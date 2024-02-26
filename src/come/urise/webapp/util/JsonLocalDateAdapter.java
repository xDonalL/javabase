package come.urise.webapp.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;

public class JsonLocalDateAdapter extends TypeAdapter<LocalDate> {
    @Override
    public void write(JsonWriter out, LocalDate value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.toString());
        }
    }

    @Override
    public LocalDate read(JsonReader in) throws IOException {
        if (in.peek() == null) {
            in.nextNull();
            return null;
        } else {
            return LocalDate.parse(in.nextString());
        }
    }
}
