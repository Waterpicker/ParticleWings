package org.waterpicker.particlewings;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.waterpicker.particlewings.config.WingConfig;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.waterpicker.particlewings.IOUtil.*;

public class Wings {


    private static Map<String, Wing> wings = new HashMap<>();

    public static void addWing(String name, URI file) throws IOException {
        loadWing(name, file).ifPresent(texture -> {
            wings.put(name, texture);
        });
    }

    private static Optional<Wing> loadWing(String name, URI file) {
        return loadZip(file).map(zip -> {
            ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().setPath(zip.getPath("attributes.conf")).build();

            Optional<BufferedImage> left;
            Optional<BufferedImage> right;
            Sequence sequence;

            try {
                WingConfig config = loader.load().getValue(TypeToken.of(WingConfig.class), new WingConfig());

                left = loadImage(zip, config.left);
                right = loadImage(zip, config.right);

                if(!left.isPresent() && !right.isPresent()) return null;

                int min = Math.min(config.angle1, config.angle2);
                int max = Math.max(config.angle1, config.angle2);

                if(min == max) {
                    sequence = new Static(min);
                } else {
                    sequence = new Oscillate(min, max);
                }
            } catch (IOException | ObjectMappingException e) {
                return null;
            }

            return new Wing(name, sequence, left, right);
        });
    }

    private static Optional<FileSystem> loadZip(URI file) {
        return zip.apply(file);
    }

    public static Wing getWing(String name) {
        return wings.get(name);
    }

    private static Optional<BufferedImage> loadImage(FileSystem system, String file){
        Path path = system.getPath("/" + file);
        return image.apply(path);

    }

    public static void increment() {
        wings.forEach((name, wing) -> wing.increment());
    }

    public static Set<String> getAvailableWings() {
        return wings.keySet();
    }
}
