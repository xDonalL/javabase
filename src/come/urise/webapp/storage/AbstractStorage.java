package come.urise.webapp.storage;

import come.urise.webapp.exception.ExistStorageException;
import come.urise.webapp.exception.NotExitStorageException;
import come.urise.webapp.model.Resume;

import java.util.Comparator;

public abstract class AbstractStorage<SK> implements Storage {

    public Resume get(String uuid) {
        SK searchKey = getNotExistedSearchKey(uuid);
        return doGet(searchKey);
    }

    public void update(Resume resume) {
        SK searchKey = getNotExistedSearchKey(resume.getUuid());
        doUpdate(searchKey, resume);
    }

    @Override
    public void save(Resume resume) {
        SK searchKey = getExistedSearchKey(resume.getUuid());
        doSave(searchKey, resume);
    }

    @Override
    public void delete(String uuid) {
        SK searchKey = getNotExistedSearchKey(uuid);
        doDelete(searchKey);
    }

    private SK getExistedSearchKey(String uuid) {
        SK searchKey = getSearchKey(uuid);
        if (isExit(searchKey)) {
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }

    private SK getNotExistedSearchKey(String uuid) {
        SK searchKey = getSearchKey(uuid);
        if (!isExit(searchKey)) {
            throw new NotExitStorageException(uuid);
        }
        return searchKey;
    }

    protected abstract SK getSearchKey(String uuid);

    protected abstract boolean isExit(SK key);

    protected abstract Resume doGet(SK searchKey);

    protected abstract void doSave(SK searchKey, Resume resume);

    protected abstract void doDelete(SK searchKey);

    protected abstract void doUpdate(SK searchKey, Resume resume);

    protected static final Comparator<Resume> SORT_BY_NAME = (o1, o2) -> {
        int result = o1.getFullName().compareTo(o2.getFullName());
        if (result == 0) {
            return o1.getUuid().compareTo(o2.getUuid());
        }
        return result;
    };
}
