package come.urise.webapp.exception;

public class StorageException extends RuntimeException {
    private final String uuid;

    StorageException(String massage, String uuid) {
        super(massage);
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }
}
