package come.urise.webapp.storage.serializer;

import come.urise.webapp.model.Resume;
import come.urise.webapp.util.JsonParser;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class JsonStreamSerializer implements StreamSerializer{
    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (BufferedWriter bf = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8))) {
            JsonParser.write(resume, bf);
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            return JsonParser.read(br, Resume.class);
        }
    }
}
