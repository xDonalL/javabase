package come.urise.webapp.storage;

import come.urise.webapp.exception.StorageException;
import come.urise.webapp.model.Resume;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFileStorage extends AbstractStorage<File> {
    private File directory;

    protected abstract void doWrite(Resume resume, OutputStream os) throws IOException;

    protected abstract Resume doRead(InputStream is) throws IOException;

    public AbstractFileStorage(File directory) {
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not writable/readable");
        }
        this.directory = directory;
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected boolean isExit(File file) {
        return file.exists();
    }

    @Override
    protected Resume doGet(File file) throws IOException {
        try {
            return doRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (FileNotFoundException e) {
            throw new StorageException("File not found", file.getName());
        }
    }

    @Override
    protected void doSave(File file, Resume resume) {
        try {
            file.createNewFile();
            doWrite(resume, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (Exception e) {
            throw new StorageException("IO Exception", file.getName(), e);
        }
    }

    @Override
    protected void doDelete(File file) {
        if (!file.delete()) {
            throw new StorageException("File delete error", file.getName());
        }
    }

    @Override
    protected void doUpdate(File file, Resume resume) {
        try {
            doWrite(resume, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (Exception e) {
            throw new StorageException("IO Exception", file.getName(), e);
        }
    }

    @Override
    public void clear() {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
    }

    @Override
    public int size() {
        File[] files = directory.listFiles();
        return files.length;
    }

    @Override
    protected List<Resume> doCopyAll() throws IOException {
        File[] files = directory.listFiles();
        if (files == null) {
            throw new StorageException("directory read error", null);
        }
        List<Resume> list = new ArrayList<>();
        for (File file : files) {
            list.add(doGet(file));
        }
        return list;
    }
}
