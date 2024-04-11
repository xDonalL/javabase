package come.urise.webapp.storage;

import come.urise.webapp.Config;
class SqlStorageTest extends AbstractStorageTest{
    public SqlStorageTest() {
        super(Config.get().getStorage());
    }
}