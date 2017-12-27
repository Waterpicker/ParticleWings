package org.waterpicker.particlewings;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class IOUtil {
    public static Function<URI, Optional<FileSystem>> zip = (path) -> {
        try {
            return Optional.of(FileSystems.newFileSystem(URI.create("jar:" + path.toString()), new HashMap<>()));
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    };

    public static Function<Path, Optional<BufferedImage>> image = (path) -> {
        try {
            return Optional.of(ImageIO.read(Files.newInputStream(path)));
        } catch (NullPointerException | IOException e) {
            return Optional.empty();
        }
    };
}
