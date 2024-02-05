package come.urise.webapp.storage;

import come.urise.webapp.exception.ExistStorageException;
import come.urise.webapp.exception.NotExitStorageException;
import come.urise.webapp.model.Resume;

import java.util.Comparator;

public abstract class AbstractStorage implements Storage {

    public Resume get(String uuid) {
        Object searchKey = getNotExistedSearchKey(uuid);
        return doGet(searchKey);
    }

    public void update(Resume resume) {
        Object searchKey = getNotExistedSearchKey(resume.getUuid());
        doUpdate(searchKey, resume);
    }

    @Override
    public void save(Resume resume) {
        Object searchKey = getExistedSearchKey(resume.getUuid());
        doSave(searchKey, resume);
    }

    @Override
    public void delete(String uuid) {
        Object searchKey = getNotExistedSearchKey(uuid);
        doDelete(searchKey);
    }

    private Object getExistedSearchKey(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (isExit(searchKey)) {
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }

    private Object getNotExistedSearchKey(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (!isExit(searchKey)) {
            throw new NotExitStorageException(uuid);
        }
        return searchKey;
    }

    protected abstract Object getSearchKey(String uuid);

    protected abstract boolean isExit(Object key);

    protected abstract Resume doGet(Object searchKey);

    protected abstract void doSave(Object searchKey, Resume resume);

    protected abstract void doDelete(Object searchKey);

    protected abstract void doUpdate(Object searchKey, Resume resume);

    protected static final Comparator<Resume> SORT_BY_NAME = (o1, o2) -> {
        int result = o1.getFullName().compareTo(o2.getFullName());
        if (result == 0) {
            return o1.getUuid().compareTo(o2.getUuid());
        }
        return result;
    };
}
