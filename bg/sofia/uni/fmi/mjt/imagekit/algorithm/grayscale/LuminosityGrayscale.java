package bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale;

import java.awt.image.BufferedImage;

public class LuminosityGrayscale implements GrayscaleAlgorithm {

    private static final double RED_COEFFICIENT = 0.21;
    private static final double GREEN_COEFFICIENT = 0.72;
    private static final double BLUE_COEFFICIENT = 0.07;
    private static final int HEX_BASE = 16;
    private static final int MASK = 0xFF;
    private static final int SHIFT_BLUE = 8;

    @Override
    public BufferedImage process(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Image cannot be null");
        }

        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage grayscaleImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);

                int red = (rgb >> HEX_BASE) & MASK;
                int green = (rgb >> SHIFT_BLUE) & MASK;
                int blue = rgb & MASK;

                int gray = (int) (RED_COEFFICIENT * red + GREEN_COEFFICIENT * green + BLUE_COEFFICIENT * blue);
                int grayscaleRGB = (gray << HEX_BASE) | (gray << SHIFT_BLUE) | gray;

                grayscaleImage.setRGB(x, y, grayscaleRGB);
            }
        }

        return grayscaleImage;
    }

}
