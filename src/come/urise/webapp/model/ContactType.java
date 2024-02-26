package come.urise.webapp.model;

public enum ContactType {
    PHONE("Тел."),
    EMAIL("Почта"),
    DISCORD("Discord"),
    SLACK("Slack"),
    GITHUB("Профиль GitHub"),
    STATCKOVERFLOW("Профиль Stackoverflow"),
    LINKEDIN("Профиль LinkedIn");

    final String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
