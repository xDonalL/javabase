package come.urise.webapp.storage;

import come.urise.webapp.model.Resume;

import java.io.IOException;
import java.util.List;

public interface Storage {

    public void clear();

    public void save(Resume r);

    public void update(Resume r);

    public Resume get(String uuid) throws IOException;

    public void delete(String uuid);

    public List<Resume> getAllSorted() throws IOException;

    public int size();
}
