package come.urise.webapp.storage;

import come.urise.webapp.exception.ExistStorageException;
import come.urise.webapp.exception.NotExitStorageException;
import come.urise.webapp.exception.StorageException;
import come.urise.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage {
    protected static final int LIMIT_STORAGE = 10000;
    protected Resume[] storage = new Resume[LIMIT_STORAGE];

    protected int size;

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            throw new NotExitStorageException(uuid);
        }
        return storage[index];
    }

    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            throw new NotExitStorageException(uuid);
        } else {
            remove(index);
            storage[size - 1] = null;
            size--;
        }
    }

    public void save(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (size >= LIMIT_STORAGE) {
            throw new StorageException( "Storage is overflow", resume.getUuid());
        } else if (index >= 0) {
            throw new ExistStorageException(resume.getUuid());
        } else {
            add(resume, index);
            size++;
        }
    }

    public void update(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index < -1) {
            throw new NotExitStorageException(resume.getUuid());
        } else {
            storage[index] = resume;
        }
    }

    public Resume[] getAll() {
        Resume[] resumes = Arrays.copyOf(storage, size);
        return resumes;
    }

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public int size() {
        return size;
    }

    protected abstract int getIndex(String uuid);

    protected abstract void add(Resume resume, int index);

    protected abstract void remove(int index);
}
