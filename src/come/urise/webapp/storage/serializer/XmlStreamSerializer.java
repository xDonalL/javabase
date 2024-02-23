package come.urise.webapp.storage.serializer;

import come.urise.webapp.model.*;
import come.urise.webapp.util.XmlParser;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class XmlStreamSerializer implements StreamSerializer {
    private XmlParser xmlParser;

    public XmlStreamSerializer() {
        xmlParser = new XmlParser(Resume.class, Link.class, Organization.class, OrganizationSection.class,
                ListSection.class, TextSection.class, Organization.Position.class);
    }

    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8))) {
            xmlParser.marshall(resume, bw);
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            return xmlParser.unmarshall(br);
        }
    }
}