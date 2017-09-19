package org.waterpicker.particlewings;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
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

    @Inject
    private PluginContainer container;

    private Map<UUID, WingEffect> effects = new HashMap<>();

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path dir;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private ConfigurationLoader<CommentedConfigurationNode> loader;

    public static ConfigManager<Config> manager;

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir.resolve("images"), "*.{png}")) {
            manager = new ConfigManager<Config>(TypeToken.of(Config.class), loader, Config::new);
            for (Path path: stream) {
                String name = path.getFileName().toString();
                int pos = name.lastIndexOf(".");
                if (pos > 0) name = name.substring(0, pos);
                Wings.addWing(name, path.toString(), false);
            }

            Wings.addWing("basic", "basic.png", true);
            Wings.addWing("dragon", "dragon.png", true);
        } catch (IOException | ObjectMappingException e) {
            e.printStackTrace();
        }

        Map<List<String>, CommandSpec> subcommands = new HashMap<>();

        subcommands.put(Lists.newArrayList("set"),
                CommandSpec.builder()
                        .arguments(new WingElement(Text.of("wing")))
                        .permission("particlewings.command.wing")
                        .executor((src, ctx) -> {
                            Optional.of(src).map(s -> (Player) s).ifPresent(player -> {
                                ctx.<String>getOne(Text.of("wing")).ifPresent(wing -> {
                                    Optional<WingEffect> effect = Optional.ofNullable(effects.get(player.getUniqueId()));

                                    if(effect.isPresent()) {
                                        if(effect.get().getWing().equals(wing)) {
                                            player.sendMessage(Text.of("Wings already set to " + wing));
                                            return;
                                        }
                                        effect.get().setWing(wing);
                                    } else {
                                        WingEffect wingEffect = new WingEffect(wing, player);
                                        effects.put(player.getUniqueId(), wingEffect);
                                        Sponge.getScheduler().createTaskBuilder().intervalTicks(manager.getConfig().interval).execute(wingEffect).submit(this);
                                    }

                                    player.sendMessage(Text.of("Wings set to " + wing));
                                });
                            });

                            return CommandResult.success();
                        }).build());

        subcommands.put(Lists.newArrayList("remove"),
                CommandSpec.builder()
                        .permission("particlewings.command.remove")
                        .executor((src, ctx) -> {
                            Optional.of(src).map(s -> (Player) s).ifPresent(player -> {
                                Optional<WingEffect> effect = Optional.ofNullable(effects.get(player.getUniqueId()));

                                if(effect.isPresent()) {
                                    effects.remove(player.getUniqueId());
                                    player.sendMessage(Text.of("Wings removed"));
                                } else {
                                    player.sendMessage(Text.of("Wings already removed"));
                                }
                            });

                            return CommandResult.success();
                        }).build());

        Sponge.getCommandManager().register(this, CommandSpec.builder().children(subcommands).build(), "particlewings", "pw");
    }

}
