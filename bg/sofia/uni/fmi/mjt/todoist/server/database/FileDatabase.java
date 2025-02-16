package bg.sofia.uni.fmi.mjt.todoist.server.database;

import bg.sofia.uni.fmi.mjt.todoist.server.database.identifiable.Identifiable;

import java.util.Map;
import java.util.HashMap;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;
import java.io.EOFException;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileNotFoundException;

public abstract class FileDatabase<T extends Identifiable & Serializable> implements Database<T> {
    private final Path filePath;
    protected final Map<String, T> objects = new HashMap<>();

    public FileDatabase(String fileName) {
        this.filePath = Path.of(fileName);

        try (var objectInputStream = new ObjectInputStream(Files.newInputStream(this.filePath))) {
            Object currentObject;

            while ((currentObject = objectInputStream.readObject()) != null) {
                T object = (T) currentObject;
                objects.put(object.getId(), object);
            }
        } catch (EOFException e) {
            // EMPTY BODY
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("The files does not exist", e);
        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while reading from a file", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save() {
        try (var objectOutputStream = new ObjectOutputStream(Files.newOutputStream(this.filePath))) {
            for (T object : objects.values()) {
                objectOutputStream.writeObject(object);
                objectOutputStream.flush();
            }
        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while writing to a file", e);
        }
    }
}