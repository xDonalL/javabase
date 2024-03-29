package come.urise.webapp.storage;

import come.urise.webapp.model.Resume;

public class ArrayStorage extends AbstractArrayStorage {

    public ArrayStorage() {
        storage = new Resume[LIMIT_STORAGE];
    }

    @Override
    protected void add(Resume resume, int index) {
        storage[size] = resume;
    }

    @Override
    protected void remove(int index) {
        storage[index] = storage[size - 1];
    }

    protected Integer getSearchKey(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}