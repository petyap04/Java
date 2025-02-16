package bg.sofia.uni.fmi.mjt.todoist.server.database;

import bg.sofia.uni.fmi.mjt.todoist.server.database.identifiable.Identifiable;

import java.io.Serializable;

public interface Database<T extends Identifiable & Serializable> {
    void save();
}
