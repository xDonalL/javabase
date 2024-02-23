package come.urise.webapp.storage;

import come.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.Comparator;

public class SortedArrayStorage extends AbstractArrayStorage {

    private static final Comparator<Resume> SORT_UUID = (o1, o2) -> o1.getUuid().compareTo(o2.getUuid());

    public SortedArrayStorage() {
        storage = new Resume[LIMIT_STORAGE];
    }

    @Override
    protected void add(Resume resume, int index) {
        int insertIndex = -index - 1;
        System.arraycopy(storage, insertIndex, storage, insertIndex + 1, size - insertIndex);
        storage[insertIndex] = resume;
    }

    @Override
    protected void remove(int index) {
        if (index != (size - 1)) {
            System.arraycopy(storage, index + 1, storage, index, size - index - 1);
        }
    }

    @Override
    protected Integer getSearchKey(String uuid) {
        Resume searchKey = new Resume(uuid, "new Name");
        return Arrays.binarySearch(storage, 0, size, searchKey, SORT_UUID);
    }
}
