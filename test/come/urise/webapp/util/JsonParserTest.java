package come.urise.webapp.util;

import come.urise.webapp.model.Resume;
import come.urise.webapp.model.Section;
import come.urise.webapp.model.TextSection;
import org.junit.jupiter.api.Test;

import static come.urise.webapp.TestData.resume1;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonParserTest {

    @Test
    void read() {
        String json = JsonParser.write(resume1);
        System.out.println(json);
        Resume resume = JsonParser.read(json, Resume.class);
        assertEquals(resume1, resume);
    }

    @Test
    void write() {
        Section section1 = new TextSection("Objective1");
        String json = JsonParser.write(section1, Section.class);
        System.out.println(json);
        Section section2 = JsonParser.read(json, Section.class);
        assertEquals(section1, section2);
    }
}