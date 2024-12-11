package bg.sofia.uni.fmi.mjt.imagekit.filesystem;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public interface FileSystemImageManager {

    BufferedImage loadImage(File imageFile) throws IOException;

    List<BufferedImage> loadImagesFromDirectory(File imagesDirectory) throws IOException;

    void saveImage(BufferedImage image, File imageFile) throws IOException;
}