package come.urise.webapp.util;

import come.urise.webapp.model.ContactType;
import come.urise.webapp.model.Organization;

public class HtmlUtil {
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static String formatDates(Organization.Position position) {
        return DateUtil.format(position.getStartDate()) + " - " + DateUtil.format(position.getEndDate());
    }

    private static String toHtmlContacts (ContactType type, String value) {
        switch (type) {
            case PHONE:
            case EMAIL:
            case DISCORD:
            case SLACK:
                return "<a>  " + type.getTitle() + ": " + value + "</a>";
            case GITHUB:
            case LINKEDIN:
            case STATCKOVERFLOW:
                return String.format("<a href= %s > %s </a>", value, type.getTitle());
            default:
                throw new IllegalArgumentException("Unsupported contact type: " + type);
        }
    }

    public static String toFormatedContacts(ContactType type, String value) {
       return value==null ? "" : toHtmlContacts(type, value);
    }
}
