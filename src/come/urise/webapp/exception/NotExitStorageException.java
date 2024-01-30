package come.urise.webapp.exception;

public class NotExitStorageException extends StorageException {
    public NotExitStorageException(String uuid) {
        super("Resume " + uuid + " not exist", uuid);
    }
}
