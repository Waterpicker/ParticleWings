package org.waterpicker.particlewings;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class IOUtil {
    public static Function<String, Optional<ZipFile>> zip = (path) -> {
        try {
            return Optional.of(new ZipFile(path));
        } catch (IOException | NullPointerException e) {
            System.out.println(path + " not found and triggered the " + e.getClass().getSimpleName());
            return Optional.empty();
        }
    };

    public static BiFunction<ZipFile, String, Optional<ZipEntry>> entry = (zipFile, name) -> {
        try {
            return Optional.of(zipFile.getEntry(name));
        } catch (NullPointerException e) {
            System.out.println(name + " not found in " + zipFile.getName() + ".");
            return Optional.empty();
        }
    };

    public static BiFunction<ZipFile, ZipEntry, Optional<InputStream>> intputStream = (zipFile, zipEntry) -> {
        try {
            return Optional.of(zipFile.getInputStream(zipEntry));
        } catch (NullPointerException | IOException e) {
            System.out.println("Couldn't convert entry " + zipEntry.getName() + " in " + zipFile.getName() + " to an InputStream.");
            return Optional.empty();
        }
    };

    public static Function<InputStream, Optional<BufferedImage>> image = (stream) -> {
        try {
            return Optional.of(ImageIO.read(stream));
        } catch (NullPointerException | IOException e) {
            System.out.println("InputStream couldn't be converted to BufferedImage.");
            return Optional.empty();
        }
    };
}
