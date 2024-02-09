package come.urise.webapp.storage;

import come.urise.webapp.exception.StorageException;
import come.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractArrayStorage extends AbstractStorage<Integer> {
    protected int size;
    protected static final int LIMIT_STORAGE = 10000;
    protected Resume[] storage = new Resume[LIMIT_STORAGE];

    @Override
    protected void doDelete(Integer searchKey) {
        remove(searchKey);
        storage[size - 1] = null;
        size--;
    }

    @Override
    protected void doSave(Integer searchKey, Resume resume) {
        if (size >= LIMIT_STORAGE) {
            throw new StorageException("Storage is overflow", resume.getUuid());
        } else {
            add(resume, searchKey);
            size++;
        }
    }

    @Override
    protected Resume doGet(Integer searchKey) {
        return storage[searchKey];
    }

    @Override
    protected void doUpdate(Integer searchKey, Resume resume) {
        storage[searchKey] = resume;
    }

    @Override
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    protected List<Resume> doCopyAll() {
        return Arrays.asList(Arrays.copyOfRange(storage, 0, size));
    }

    public int size() {
        return size;
    }

    @Override
    protected boolean isExit(Integer key) {
        return key >= 0;
    }

    protected abstract void add(Resume resume, int index);

    protected abstract void remove(int index);

}