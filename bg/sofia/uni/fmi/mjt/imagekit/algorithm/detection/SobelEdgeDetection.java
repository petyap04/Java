package bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.ImageAlgorithm;

import java.awt.image.BufferedImage;

public class SobelEdgeDetection implements EdgeDetectionAlgorithm {

    private final ImageAlgorithm grayscaleAlgorithm;

    public SobelEdgeDetection(ImageAlgorithm grayscaleAlgorithm) {
        this.grayscaleAlgorithm = grayscaleAlgorithm;
    }

    private static final int[][] GX = {
            {-1, 0, 1},
            {-2, 0, 2},
            {-1, 0, 1}
    };

    private static final int[][] GY = {
            {-1, -2, -1},
            { 0,  0,  0},
            { 1,  2,  1}
    };

    private static final int MAX_COLOR_VALUE = 255;
    private static final int HEX_BASE = 16;
    private static final int MASK = 0xFF;
    private static final int SHIFT_BLUE = 8;

    @Override
    public BufferedImage process(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Image cannot be null");
        }
        BufferedImage grayscale = grayscaleAlgorithm.process(image);
        int width = grayscale.getWidth();
        int height = grayscale.getHeight();
        BufferedImage edgeImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int gx = 0;
                int gy = 0;
                for (int ky = -1; ky <= 1; ky++) {
                    for (int kx = -1; kx <= 1; kx++) {
                        int pixel = (grayscale.getRGB(x + kx, y + ky) & MASK);
                        gx += pixel * GX[ky + 1][kx + 1];
                        gy += pixel * GY[ky + 1][kx + 1];
                    }
                }
                int magnitude = Math.min((int) Math.sqrt(gx * gx + gy * gy), MAX_COLOR_VALUE);
                int edgeRGB = (magnitude << HEX_BASE) | (magnitude << SHIFT_BLUE) | magnitude;
                edgeImage.setRGB(x, y, edgeRGB);
            }
        }
        return edgeImage;
    }

}
