package come.urise.webapp.storage;

import come.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapUuidStorage extends AbstractStorage {

    private final Map<String, Resume> storage = new HashMap<>();

    @Override
    protected Object getSearchKey(String uuid) {
        for (String key : storage.keySet()) {
            if (key.equals(uuid)) {
                return key;
            }
        }
        return uuid;
    }

    @Override
    protected boolean isExit(Object key) {
        for (String k : storage.keySet()) {
            if (key.equals(k)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected Resume doGet(Object searchKey) {
        return storage.get(searchKey);
    }

    @Override
    protected void doSave(Object searchKey, Resume resume) {
        storage.put((String) searchKey, resume);
    }

    @Override
    protected void doDelete(Object searchKey) {
        storage.remove(searchKey);
    }

    @Override
    protected void doUpdate(Object searchKey, Resume resume) {
        storage.put((String) searchKey, resume);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> storageCopy = new ArrayList<>(size());
        storageCopy.addAll(storage.values());
        storageCopy.sort(SORT_BY_NAME);
        return storageCopy;
    }

    @Override
    public int size() {
        return storage.size();
    }
}
