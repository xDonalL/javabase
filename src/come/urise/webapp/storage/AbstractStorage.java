package come.urise.webapp.storage;

import come.urise.webapp.exception.ExistStorageException;
import come.urise.webapp.exception.NotExitStorageException;
import come.urise.webapp.model.Resume;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractStorage<SK> implements Storage {

    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());

    public Resume get(String uuid) throws IOException {
        LOG.info("Get " + uuid);
        SK searchKey = getNotExistedSearchKey(uuid);
        return doGet(searchKey);
    }

    public void update(Resume resume) {
        LOG.info("Update " + resume);
        SK searchKey = getNotExistedSearchKey(resume.getUuid());
        doUpdate(searchKey, resume);
    }

    @Override
    public void save(Resume resume) {
        LOG.info("Save " + resume);
        SK searchKey = getExistedSearchKey(resume.getUuid());
        doSave(searchKey, resume);
    }

    @Override
    public void delete(String uuid) {
        LOG.info("Delete " + uuid);
        SK searchKey = getNotExistedSearchKey(uuid);
        doDelete(searchKey);
    }

    private SK getExistedSearchKey(String uuid) {
        SK searchKey = getSearchKey(uuid);
        if (isExit(searchKey)) {
            LOG.warning("Resume " + uuid + " already exist");
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }

    private SK getNotExistedSearchKey(String uuid) {
        SK searchKey = getSearchKey(uuid);
        if (!isExit(searchKey)) {
            LOG.warning("Resume " + uuid + " not exist");
            throw new NotExitStorageException(uuid);
        }
        return searchKey;
    }

    @Override
    public List<Resume> getAllSorted() throws IOException {
        LOG.info("AllSorted");
        List<Resume> list = doCopyAll();
        list.sort((o1, o2) -> {
            int result = o1.getFullName().compareTo(o2.getFullName());
            if (result == 0) {
                return o1.getUuid().compareTo(o2.getUuid());
            }
            return result;
        });
        return list;
    }

    protected abstract List<Resume> doCopyAll() throws IOException;

    protected abstract SK getSearchKey(String uuid);

    protected abstract boolean isExit(SK key);

    protected abstract Resume doGet(SK searchKey) throws IOException;

    protected abstract void doSave(SK searchKey, Resume resume);

    protected abstract void doDelete(SK searchKey);

    protected abstract void doUpdate(SK searchKey, Resume resume);
}
