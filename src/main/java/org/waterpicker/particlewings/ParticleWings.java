package org.waterpicker.particlewings;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.waterpicker.particlewings.config.Config;
import org.waterpicker.particlewings.config.ConfigManager;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;


@Plugin(
        id = "particlewings",
        name = "ParticleWings",
        authors = {
                "Waterpicker"
        }
)
public class ParticleWings {

    @Inject
    private Logger logger;

    private static PluginContainer container;

    private Map<UUID, Wing> effects = new HashMap<>();

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path dir;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private ConfigurationLoader<CommentedConfigurationNode> loader;

    public static ConfigManager<Config> manager;

    @Inject
    public ParticleWings(PluginContainer container) {
        ParticleWings.container = container;
    }

    private int count = 0;

    private boolean isDisabled = false;

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        setupWings();

        try {
            manager = new ConfigManager<>(TypeToken.of(Config.class), loader, Config::new);
        } catch (IOException | ObjectMappingException e) {
            e.printStackTrace();
        }

        Map<List<String>, CommandSpec> subcommands = new HashMap<>();
        subcommands.put(Lists.newArrayList("deactivate"),
                CommandSpec.builder()
                        .permission("particlewings.command.use")
                        .executor((src, ctx) -> {
                            Optional.of(src).map(s -> (Player) s).ifPresent(player -> {
                                Optional<Wing> effect = Optional.ofNullable(effects.get(player.getUniqueId()));

                                if(effect.isPresent()) {
                                    effects.remove(player.getUniqueId());
                                    player.sendMessage(Text.of("Wings deactivated"));
                                } else {
                                    player.sendMessage(Text.of("Wings already inactive."));
                                }
                            });

                            return CommandResult.success();
                        }).build());

        subcommands.put(Lists.newArrayList("disable"),
                CommandSpec.builder()
                        .permission("particlewings.command.disable")
                        .executor((src, ctx) -> {
                            isDisabled = !isDisabled;

                            if (isDisabled) {
                                Sponge.getServer().getBroadcastChannel().send(Text.of("Wings are now disabled."));
                                effects.clear();
                            } else Sponge.getServer().getBroadcastChannel().send(Text.of("Wings are now enabled"));

                            return CommandResult.success();
                        })
                        .build());

        CommandSpec spec = CommandSpec.builder()
                .arguments(new WingElement(Text.of("wing")))
                .permission("particlewings.command.use")
                .executor((src, ctx) -> {
                    Optional.of(src).map(s -> (Player) s).ifPresent(player -> {
                        ctx.<Wing>getOne(Text.of("wing")).ifPresent(wing -> {
                            Optional<Wing> effect = Optional.ofNullable(effects.get(player.getUniqueId()));
                            if(effect.isPresent()) {
                                if(effect.get().equals(wing)) {
                                    effects.remove(player.getUniqueId());
                                    player.sendMessage(Text.of("Wings deactivated"));
                                } else {
                                    changeWing(player, wing);
                                }
                            } else {
                                changeWing(player, wing);
                            }
                        });
                    });
                    return CommandResult.success();
                })
                .children(subcommands)
                .build();

        Sponge.getCommandManager().register(this, spec, "particlewings", "pw");
    }

    private void changeWing(Player player, Wing wing) {
        if(isDisabled) {
            if (effects.isEmpty()) {
                Sponge.getScheduler().createTaskBuilder().async().intervalTicks(manager.getConfig().interval).execute((task) -> {
                    if (effects.isEmpty()) {
                        task.cancel();
                        return;
                    }

                    effects.forEach((uuid, w) -> {
                        Sponge.getServer().getPlayer(uuid).filter(ParticleWings::isInvisble).ifPresent(p -> w.render(p.getWorld(), p.getTransform()));
                    });
                    Wing.increment();
                }).submit(this);
            }

            effects.put(player.getUniqueId(), wing);
            player.sendMessage(Text.of("Wings set to " + wing));
        } else {
            player.sendMessage(Text.of("Wings are currently disabled."));
        }
    }

    @Listener
    public void onLogOff(ClientConnectionEvent.Disconnect event) {
        effects.remove(event.getTargetEntity().getUniqueId());
    }

    private void setupWings() {
        Path directory = dir.resolve("wings");
        if (!directory.toFile().exists()) {
            directory.toFile().mkdir();
        }

        try(DirectoryStream<Path> stream = Files.newDirectoryStream(directory, "*.{zip}")) {
            for (Path path : stream) {
                String name = path.getFileName().toString();
                int pos = name.lastIndexOf(".");
                if (pos > 0) name = name.substring(0, pos);
                System.out.println(path.toAbsolutePath());
                Wings.addWing(name, path.toUri());
            }
        } catch  (IOException e) {
            logger.error("Directory couldn't be created");
        }
    }

    public static PluginContainer getContainer() {
        return container;
    }

    public static boolean isInvisble(Player player) {
        return !(player.get(Keys.INVISIBLE).orElse(false) || player.get(Keys.VANISH).orElse(false));
    }
}
