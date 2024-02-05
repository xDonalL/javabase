package come.urise.webapp.storage;

import come.urise.webapp.exception.StorageException;
import come.urise.webapp.model.Resume;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class SortedArrayStorageTest extends AbstractStorageTest {

    public SortedArrayStorageTest() {
        super(new SortedArrayStorage());
    }
    @Test
    public void storageOverflow() {
        assertThrows(StorageException.class, () -> {
            for (int i = 3; i < 10001; i++) {
                storage.save(new Resume("user"+i));
            }
        });
    }
}