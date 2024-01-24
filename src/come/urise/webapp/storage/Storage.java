package come.urise.webapp.storage;

import come.urise.webapp.model.Resume;

public interface Storage {

    public void clear();

    public void save(Resume r);

    public void update(Resume r);

    public Resume get(String uuid);

    public void delete(String uuid);

    public Resume[] getAll();

    public int size();
}
