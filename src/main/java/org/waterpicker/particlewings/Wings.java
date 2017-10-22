package org.waterpicker.particlewings;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.zip.ZipFile;

import static org.waterpicker.particlewings.IOUtil.*;

public class Wings {


    private static Map<String, Wing> wings = new HashMap<>();

    public static void addWing(String file) throws IOException {
        addWing(file.split(".")[0], file, true);
    }

    public static void addWing(String name, String file) throws IOException {
        addWing(name, file, false);
    }

    private static void addWing(String name, String file, boolean isInJar) throws IOException {
        loadWing(name, file, isInJar).ifPresent(texture -> {
            wings.put(name, texture);
        });
    }

    private static Optional<Wing> loadWing(String name, String file, boolean isInJar) {
        Optional<FileSystem> zip = loadZip(file, isInJar);

        if(zip.isPresent()) {
            Optional<BufferedImage> both = loadImage(zip.get(), "both.png");

            if(both.isPresent()) {
                return Optional.of(new Wing(name, both.get(), both.get()));
            }

            Optional<BufferedImage> left = loadImage(zip.get(), "left.png");
            Optional<BufferedImage> right = loadImage(zip.get(), "right.png");

            if(left.isPresent() && right.isPresent()) {
                return Optional.of(new Wing(name, left.get(), right.get()));
            } else {
                System.out.println("Wing " + name + " wasn't created.");
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    private static Optional<FileSystem> loadZip(String file, boolean isInJar) {
        if(isInJar) {
            return zip.apply(ParticleWings.getContainer().getAsset(file).map(a -> a.getUrl().getPath()).orElse(null));
        } else {
            return zip.apply(file);
        }
    }

    public static Wing getWing(String name) {
        return wings.get(name);
    }

    private static Optional<BufferedImage> loadImage(FileSystem system, String file){
        Path path = system.getPath("/" + file);
        System.out.println(path);
        return image.apply(path);

    }

    public static Set<String> getAvailableWings() {
        return wings.keySet();
    }
}
