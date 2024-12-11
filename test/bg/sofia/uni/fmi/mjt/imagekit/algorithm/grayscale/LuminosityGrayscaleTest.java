package bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale;

import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

public class LuminosityGrayscaleTest {

    @Test
    public void testProcessGrayscaleSuccess() {
        LuminosityGrayscale grayscale = new LuminosityGrayscale();

        BufferedImage colorImage = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        colorImage.setRGB(0, 0, 0xFF0000);
        colorImage.setRGB(1, 0, 0x00FF00);
        colorImage.setRGB(0, 1, 0x0000FF);
        colorImage.setRGB(1, 1, 0xFFFFFF);

        BufferedImage grayscaleImage = grayscale.process(colorImage);

        assertEquals(0x4D4D4D, grayscaleImage.getRGB(0, 0), "Red should convert correctly");
        assertEquals(0xB6B6B6, grayscaleImage.getRGB(1, 0), "Green should convert correctly");
        assertEquals(0x121212, grayscaleImage.getRGB(0, 1), "Blue should convert correctly");
        assertEquals(0xFFFFFF, grayscaleImage.getRGB(1, 1), "White should remain white");
    }

    @Test
    public void testProcessWithNullImage() {
        LuminosityGrayscale grayscale = new LuminosityGrayscale();
        assertThrows(IllegalArgumentException.class, () -> grayscale.process(null));
    }
}
