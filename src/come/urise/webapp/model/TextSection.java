package come.urise.webapp.model;

import java.util.Objects;

public class TextSection extends Section {
    private final String content;

    public TextSection(String text) {
        Objects.requireNonNull(text, "text must not be null");
        this.content = text;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextSection that = (TextSection) o;
        return content.equals(that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }
}
