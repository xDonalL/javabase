package come.urise.webapp.storage;

import come.urise.webapp.storage.serializer.ObjectStreamSerializer;

class PathStorageTest extends AbstractStorageTest {
    public PathStorageTest() {
        super(new PathStorage(STORAGE_DIR, new ObjectStreamSerializer()));
    }
}