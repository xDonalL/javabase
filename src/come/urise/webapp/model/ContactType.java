package come.urise.webapp.model;

import java.io.Serializable;

public enum ContactType implements Serializable {
    PHONE("Тел."),
    EMAIL("Почта"),
    DISCORD("Discord"),
    SLACK("Slack"),
    GITHUB("Профиль GitHub"),
    STATCKOVERFLOW("Профиль Stackoverflow"),
    LINKEDIN("Профиль LinkedIn");

    String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
