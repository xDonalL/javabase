package come.urise.webapp.storage;

import come.urise.webapp.model.Resume;

public abstract class AbstractArrayStorage implements Storage {
    protected static final int LIMIT_STORAGE = 10000;
    protected Resume[] storage = new Resume[LIMIT_STORAGE];

    protected int size;

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index == -1) {
            System.out.println("Resume does not exist");
            return null;
        }
        return storage[index];
    }

    public int size() {
        return size;
    }

    protected abstract int getIndex(String uuid);
}
