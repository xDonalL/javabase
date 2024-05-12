package come.urise.webapp.web;

import come.urise.webapp.Config;
import come.urise.webapp.model.*;
import come.urise.webapp.storage.Storage;
import come.urise.webapp.util.DateUtil;
import come.urise.webapp.util.HtmlUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResumeServlet extends HttpServlet {
    Storage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.get().getStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
        }
        Resume r;
        if (action != null) {
            switch (action) {
                case "delete":
                    if(!Config.get().isImmutable(uuid)) {
                        storage.delete(uuid);
                    }
                    response.sendRedirect("resume");
                    return;
                case "add":
                    r = Resume.EMPTY;
                    break;
                case "edit":
                    r = storage.get(uuid);
                    for (SectionType type : new SectionType[]{SectionType.EXPERIENCE, SectionType.EDUCATION}) {
                        OrganizationSection section = (OrganizationSection) r.getSections().get(type);
                        List<Organization> emptyFirstOrganizations = new ArrayList<>();
                        emptyFirstOrganizations.add(Organization.EMPTY);
                        if (section != null) {
                            for (Organization org : section.getOrganizations()) {
                                List<Organization.Position> emptyFirstPositions = new ArrayList<>();
                                emptyFirstPositions.add(Organization.Position.EMPTY);
                                emptyFirstPositions.addAll(org.getPositions());
                                Organization.Position[] emptyFirstPosition = emptyFirstPositions.toArray(
                                        new Organization.Position[emptyFirstPositions.size()]);
                                emptyFirstOrganizations.add(new Organization(org.getHomePage(), emptyFirstPosition));
                            }
                        }
                        r.addSections(type, new OrganizationSection(emptyFirstOrganizations));
                    }
                    break;
                case "view":
                    r = storage.get(uuid);
                    break;
                default:
                    throw new javax.servlet.ServletException("Invalid action: " + action);
            }
            request.setAttribute("resume", r);
            request.getRequestDispatcher("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
                    .forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        boolean isCreate = HtmlUtil.isEmpty(uuid);
        Resume r;
        if (isCreate) {
            r = new Resume(fullName);
        } else {
            r = storage.get(uuid);
            r.setFullName(fullName);
        }
        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (!HtmlUtil.isEmpty(value)) {
                r.addContacts(type, value);
            } else {
                r.getContacts().remove(type);
            }
        }
        for (SectionType type : SectionType.values()) {
            String value = request.getParameter(type.name());
            String[] values = request.getParameterValues(type.name());
            if (HtmlUtil.isEmpty(value) && values.length < 2) {
                r.getSections().remove(type);
            } else {
                switch (type) {
                    case OBJECTIVE:
                    case PERSONAL:
                        r.addSections(type, new TextSection(value));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        r.addSections(type, new ListSection(Arrays.asList(value.split("\\n"))));
                        break;
                    case EDUCATION:
                    case EXPERIENCE:
                        List<Organization> orgs = new ArrayList<>();
                        String[] urls = request.getParameterValues(type.name() + "url");
                        for (int i = 0; i < values.length; i++) {
                            String name = values[i];
                            if (!HtmlUtil.isEmpty(name)) {
                                List<Organization.Position> positions = new ArrayList<>();
                                String pfx = type.name() + i;
                                String[] titles = request.getParameterValues(pfx + "title");
                                String[] startDates = request.getParameterValues(pfx + "startDate");
                                String[] endDates = request.getParameterValues(pfx + "endDate");
                                String[] descriptions = request.getParameterValues(pfx + "description");
                                for (int j = 0; j < titles.length; j++) {
                                    if (!HtmlUtil.isEmpty(titles[j])) {
                                        positions.add(new Organization.Position(DateUtil.parse(startDates[j]), DateUtil.parse(endDates[j]), titles[j], descriptions[j]));
                                    }
                                }
                                orgs.add(new Organization(name, urls[i], positions.toArray(new Organization.Position[positions.size()])));
                            }
                        }
                        r.addSections(type, new OrganizationSection(orgs));
                        break;
                }
            }
        }
        if (isCreate) {
            storage.save(r);
        } else {
            if (!Config.get().isImmutable(uuid)) {
            storage.update(r);
            }
        }
        if (HtmlUtil.isEmpty(r.getFullName())) {
            storage.delete(r.getUuid());
        }
        response.sendRedirect("resume");
    }
}
