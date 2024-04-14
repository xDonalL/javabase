package come.urise.webapp.exception;

public class StorageException extends RuntimeException {
    private final String uuid;

    public StorageException(String massage, String uuid) {
        this(massage, uuid, null);
    }

    public StorageException(Exception e) {
        this(null, null, e);
    }

    public StorageException(String massage) {
        this(massage, null, null);
    }

    public StorageException(String massage, String uuid, Exception e) {
        super(massage, e);
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }
}
