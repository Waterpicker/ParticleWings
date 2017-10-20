package org.waterpicker.particlewings;

import java.awt.image.BufferedImage;
import java.io.IOException;
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
        Optional<ZipFile> zip = loadZip(file, isInJar);

        if(zip.isPresent()) {
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

    private static Optional<ZipFile> loadZip(String file, boolean isInJar) {
        if(isInJar) {
            return zip.apply(ParticleWings.getContainer().getAsset(file).map(a -> a.getUrl().getPath()).orElse(null));
        } else {
            return zip.apply(file);
        }
    }

    public static Wing getWing(String name) {
        return wings.get(name);
    }

    private static Optional<BufferedImage> loadImage(ZipFile zipFile, String file){
        return image.apply(intputStream.apply(zipFile, entry.apply(zipFile, file).orElse(null)).orElse(null));

    }

    public static Set<String> getAvailableWings() {
        return wings.keySet();
    }
}
