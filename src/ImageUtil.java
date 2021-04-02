import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

/**
 * Utility functions for dealing with images and colors.
 */
class ImageUtil {

    /**
     * Checks whether the given values are valid as an RGB color.
     */
    static boolean validRgb(int r, int g, int b) {
        return validRgbComponent(r) && validRgbComponent(g) && validRgbComponent(b);
    }

    /**
     * Checks whether the given value is valid as an RGB component.
     */
    static boolean validRgbComponent(int value) {
        return value >= 0 && value <= 255;
    }

    /**
     * Checks whether the given RGB values are valid, and throws an
     * IllegalArgumentException if not.
     */
    static void validateRgb(int r, int g, int b) {
        if(!validRgb(r, g, b))
            throw new IllegalArgumentException("invalid RGB: " + r + ", " + g + ", " + b);
    }

    /**
     * Encodes RGB values into a 32-bit integer.
     */
    static int encodeRgb(int r, int g, int b) {
        validateRgb(r, g, b);

        return 255 << 24 | r << 16 | g << 8 | b;
    }

    /**
     * Decodes an integer into an array with the RGB values, performing the
     * inverse operation the encodeRgb(r, g, b) method.
     */
    static int[] decodeRgb(int value) {
        int[] rgb = {
                (value >> 16) & 0xFF,
                (value >> 8) & 0xFF,
                value & 0xFF
        };
        if(!validRgb(rgb[0], rgb[1], rgb[2]))
            throw new IllegalArgumentException("Invalid value: " + value +
                    ", resulted in " + Arrays.toString(rgb));
        return rgb;
    }

    /**
     * Obtains the luminance of an RGB color in the interval [0, 255].
     */
    static int luminance(int r, int g, int b) {
        validateRgb(r, g, b);
        return (int) Math.round(r*.21 + g*.71 + b*.08);
    }


    static void validateFile(File file) {
        if(!file.exists())
            throw new IllegalArgumentException("file does not exist");
        if(!file.isFile())
            throw new IllegalArgumentException("path does represent a file");
    }

    /**
     * Constructs a boolean matrix representing the pixels of an image file (GIF, PNG, JPG) in binary mode.
     *
     * The number of matrix lines corresponds to the image height, whereas
     * the number of matrix columns corresponds to the image width.
     *
     * Image pixels with luminance above 50% will have the corresponding boolean value set to true,
     * whereas false otherwise.
     */
    static boolean[][] readBinaryImage(String imagePath) {
        File file = new File(imagePath);
        validateFile(file);
        try {
            BufferedImage img = ImageIO.read(file);
            int[] pixels = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
            boolean[][] data = new boolean[img.getHeight()][img.getWidth()];
            int i = 0;
            for(int y = 0; y < data.length; y++) {
                for(int x = 0; x < data[y].length; x++) {
                    int v = pixels[i++];
                    int r = (v >> 16) & 0xFF;
                    int g = (v >> 8) & 0xFF;
                    int b = v & 0xFF;
                    data[y][x] = luminance(r, g, b) >= 128;
                }
            }
            return data;
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }


    /**
     * Constructs an integer matrix with pixel colors from an image file (GIF, PNG, JPG).
     *
     * The number of matrix lines corresponds to the image height, whereas
     * the number of matrix columns corresponds to the image width.
     *
     * Colors are encoded as 32-bit integers.
     *
     * 00000000 00000000 00000000 00000000
     * alpha		red      green    blue
     */
    static int[][] readColorImage(String imagePath) {
        File file = new File(imagePath);
        validateFile(file);
        try {
            BufferedImage img = ImageIO.read(file);
            int[] pixels = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
            int[][] data = new int[img.getHeight()][img.getWidth()];
            int i = 0;
            for(int y = 0; y < data.length; y++) {
                for(int x = 0; x < data[y].length; x++) {
                    int v = pixels[i++];
                    int r = (v >> 16) & 0xFF;
                    int g = (v >> 8) & 0xFF;
                    int b = v & 0xFF;
                    data[y][x] = encodeRgb(r, g, b);
                }
            }
            return data;
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Writes an image to a file given its pixel data and an image format (gif, jpg, png).
     * Pixel values are expected to be encoded as in the encodeRgb(...) method.
     */
    static void writeImage(int[][] data, String path, String format) {
        if(!format.matches("gif|jpg|png"))
            throw new IllegalArgumentException("invalid format: " + format + " (valid values: gif, jpg, png)");

        BufferedImage img = new BufferedImage(data[0].length, data.length, BufferedImage.TYPE_INT_ARGB);
        for(int y = 0; y < data.length; y++)
            for(int x = 0; x < data[y].length; x++)
                img.setRGB(x, y, data[y][x]);

        File file = new File(path);
        try {
            ImageIO.write(img, format, file);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
