package bg.sofia.uni.fmi.mjt.imagekit.filesystem;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocalFileSystemImageManager implements FileSystemImageManager {

    @Override
    public BufferedImage loadImage(File imageFile) throws IOException {
        if (imageFile == null) {
            throw new IllegalArgumentException("Image file cannot be null.");
        }

        if (!imageFile.exists() || !imageFile.isFile()) {
            throw new IOException("Invalid file: file does not exist or is not a regular file.");
        }

        return ImageIO.read(imageFile);
    }

    @Override
    public List<BufferedImage> loadImagesFromDirectory(File imagesDirectory) throws IOException {
        if (imagesDirectory == null) {
            throw new IllegalArgumentException("Directory cannot be null");
        }

        if (!imagesDirectory.isDirectory()) {
            throw new IOException("Path is not a directory");
        }

        File[] files = imagesDirectory.listFiles();
        if (files == null) {
            throw new IOException("Failed to list files in the directory");
        }

        List<BufferedImage> images = new ArrayList<>();
        for (File file : files) {
            String fileName = file.getName().toLowerCase();
            if (!fileName.endsWith(".jpeg") && !fileName.endsWith(".png") && !fileName.endsWith(".bmp")) {
                throw new IOException("Unsupported file format in directory: " + fileName);
            }

            images.add(loadImage(file));
        }
        return images;
    }

    @Override
    public void saveImage(BufferedImage image, File imageFile) throws IOException {
        if (image == null || imageFile == null) {
            throw new IllegalArgumentException("Image or file cannot be null");
        }

        String fileName = imageFile.getName();
        String format = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();

        if (!format.matches("jpeg|png|bmp")) {
            throw new IOException("Unsupported file format: " + format);
        }

        boolean result = ImageIO.write(image, format, imageFile);
        if (!result) {
            throw new IOException("Failed to write the image to the file");
        }
    }

}
