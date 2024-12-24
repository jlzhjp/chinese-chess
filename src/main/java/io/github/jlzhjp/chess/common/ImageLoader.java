package io.github.jlzhjp.chess.common;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;

public class ImageLoader {
    private static final HashMap<String, Image> cache = new HashMap<>();

    public static Image load(String key) {
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        InputStream stream = Objects.requireNonNull(ImageLoader.class.getResourceAsStream(key));
        BufferedImage image;

        try {
            image = ImageIO.read(stream);
        } catch (IOException e) {
            return null;
        }

        cache.put(key, image);

        return image;
    }
}
