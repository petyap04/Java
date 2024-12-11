package bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale.LuminosityGrayscale;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

public class SobelEdgeDetectionTest {

    @Test
    public void testProcessEdgeDetectionSuccess() {
        LuminosityGrayscale grayscale = new LuminosityGrayscale();
        SobelEdgeDetection edgeDetection = new SobelEdgeDetection(grayscale);

        BufferedImage testImage = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
        testImage.setRGB(0, 0, 0xFFFFFF);
        testImage.setRGB(1, 0, 0x000000);
        testImage.setRGB(2, 0, 0xFFFFFF);

        BufferedImage edgeImage = edgeDetection.process(testImage);

        assertNotNull(edgeImage, "Edge-detected image should not be null");
        assertEquals(3, edgeImage.getWidth(), "Edge-detected image width should match");
        assertEquals(3, edgeImage.getHeight(), "Edge-detected image height should match");
    }

    @Test
    public void testProcessWithNullImage() {
        SobelEdgeDetection edgeDetection = new SobelEdgeDetection(new LuminosityGrayscale());
        assertThrows(IllegalArgumentException.class, () -> edgeDetection.process(null));
    }
}
