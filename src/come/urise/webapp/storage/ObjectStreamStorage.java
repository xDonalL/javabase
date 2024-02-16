package come.urise.webapp.storage;

import come.urise.webapp.exception.StorageException;
import come.urise.webapp.model.Resume;

import java.io.*;

public class ObjectStreamStorage extends AbstractFileStorage {

    public ObjectStreamStorage(String file) {
        super(file);
    }

    @Override
    protected void doWrite(Resume resume, OutputStream os) throws IOException {
       try (ObjectOutputStream outputStream = new ObjectOutputStream(os)) {
           outputStream.writeObject(resume);
       }
    }

    @Override
    protected Resume doRead(InputStream is) throws IOException {
        try (ObjectInputStream inputStream = new ObjectInputStream(is)){
            return (Resume) inputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new StorageException("Class not found", is.toString());
        }
    }
}
