package org.waterpicker.particlewings;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class Wings {
    private static Map<String, Texture> textures = new HashMap<>();

    public static void addWing(String name, String file, boolean isInJar) throws IOException {
        loadImage(file, isInJar).map(Texture::new).ifPresent(texture -> {
            textures.put(name, texture);
        });
    }

    public static Texture getWing(String name) {
        return textures.get(name);
    }

    private static Optional<BufferedImage> loadImage(String file, boolean isInJar) throws IOException {
        BufferedImage buff = null;

        if(isInJar) buff = ImageIO.read(ParticleWings.class.getResource("/" + file));
        else buff = ImageIO.read(new File(file));

        return Optional.ofNullable(buff);
    }

    public static Set<String> getAvailableWings() {
        return textures.keySet();
    }
}
