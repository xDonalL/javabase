package come.urise.webapp.storage;

import come.urise.webapp.exception.StorageException;
import come.urise.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage extends AbstractStorage {
    protected int size;
    protected static final int LIMIT_STORAGE = 10000;
    protected Resume[] storage = new Resume[LIMIT_STORAGE];

    @Override
    protected void doDelete(Object searchKey) {
        remove((Integer) searchKey);
        storage[size - 1] = null;
        size--;
    }

    @Override
    protected void doSave(Object searchKey, Resume resume) {
        if (size >= LIMIT_STORAGE) {
            throw new StorageException("Storage is overflow", resume.getUuid());
        } else {
            add(resume, (Integer) searchKey);
            size++;
        }
    }

    @Override
    protected Resume doGet(Object searchKey) {
        return storage[(Integer) searchKey];
    }

    @Override
    protected void doUpdate(Object searchKey, Resume resume) {
        storage[(Integer) searchKey] = resume;
    }

    @Override
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    public int size() {
        return size;
    }

    @Override
    protected boolean isExit(Object key) {
        return (Integer) key >= 0;
    }

    protected abstract void add(Resume resume, int index);

    protected abstract void remove(int index);

}