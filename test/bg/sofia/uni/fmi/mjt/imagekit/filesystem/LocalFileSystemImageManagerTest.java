package bg.sofia.uni.fmi.mjt.imagekit.filesystem;

import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class LocalFileSystemImageManagerTest {

    @Test
    public void testLoadImageSuccess() throws IOException {
        LocalFileSystemImageManager manager = new LocalFileSystemImageManager();
        File imageFile = new File("src/test/resources/sample.png");

        BufferedImage image = manager.loadImage(imageFile);

        assertNotNull(image, "Image should be successfully loaded");
    }

    @Test
    public void testLoadImageWithNullFile() {
        LocalFileSystemImageManager manager = new LocalFileSystemImageManager();
        assertThrows(IllegalArgumentException.class, () -> manager.loadImage(null));
    }

    @Test
    public void testLoadImageWithInvalidFile() {
        LocalFileSystemImageManager manager = new LocalFileSystemImageManager();
        File invalidFile = new File("src/test/resources/invalid.png");

        assertThrows(IOException.class, () -> manager.loadImage(invalidFile));
    }

    @Test
    public void testLoadImagesFromDirectorySuccess() throws IOException {
        LocalFileSystemImageManager manager = new LocalFileSystemImageManager();
        File directory = new File("src/test/resources/images");

        var images = manager.loadImagesFromDirectory(directory);

        assertNotNull(images, "Images list should not be null");
        assertFalse(images.isEmpty(), "Images list should not be empty");
    }

    @Test
    public void testLoadImagesFromInvalidDirectory() {
        LocalFileSystemImageManager manager = new LocalFileSystemImageManager();
        File invalidDirectory = new File("src/test/resources/invalid");

        assertThrows(IOException.class, () -> manager.loadImagesFromDirectory(invalidDirectory));
    }

    @Test
    public void testSaveImageSuccess() throws IOException {
        LocalFileSystemImageManager manager = new LocalFileSystemImageManager();
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        File outputFile = new File("src/test/resources/output/saved-image.png");

        manager.saveImage(image, outputFile);

        assertTrue(outputFile.exists(), "Saved image file should exist");
        outputFile.delete();
    }

    @Test
    public void testSaveImageWithNullArguments() {
        LocalFileSystemImageManager manager = new LocalFileSystemImageManager();
        assertThrows(IllegalArgumentException.class, () -> manager.saveImage(null, null));
    }
}
