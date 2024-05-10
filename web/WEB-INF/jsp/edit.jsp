<%@ page import="come.urise.webapp.model.*" %>
<%@ page import="come.urise.webapp.util.HtmlUtil" %>
<%@ page import="come.urise.webapp.util.DateUtil" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="come.urise.webapp.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <h1>Имя:</h1>
        <dl>
            <input type="text" name="fullName" size=55 value="${resume.fullName}">
        </dl>
        <h2>Контакты:</h2>
        <c:forEach var="type" items="<%=ContactType.values()%>">
            <dl>
                <dt>${type.title}</dt>
                <dd><input type="text" name="${type.name()}" size=30 value="${resume.contacts.get(type)}"></dd>
            </dl>
        </c:forEach>
        <hr>
        <% for (SectionType sectionType : SectionType.values()) {
            int counter = 0;
        %>
        <h2><%=sectionType.getTitle()%>
        </h2>
        <%
            switch (sectionType) {
                case OBJECTIVE:
                case PERSONAL:
                    if (resume.getSections().get(sectionType) == null) {
        %>

        <textarea name='<%=sectionType.name()%>' cols=75
                  rows=5 placeholder="<%=sectionType.getTitle()%>"><%=""%></textarea>
        <%
        } else {
        %>
        <textarea name='<%=sectionType.name()%>' cols=75
                  rows=5><%=resume.getSections().get(sectionType)%></textarea>
        <%
                }
                break;
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                if (resume.getSections().get(sectionType) == null) {
        %>
        <textarea name="<%=sectionType.name()%>" cols=75
                  rows=5 placeholder="<%=sectionType.getTitle()%>"><%=""%></textarea>
        <%
        } else {
        %>
        <textarea name="<%=sectionType.name()%>" cols=75
                  rows=5><%=String.join("\n", ((ListSection) resume.getSections().get(sectionType)).getItems())%></textarea>
        <%
                }
                break;
            case EXPERIENCE:
            case EDUCATION:
                for (Organization org : ((OrganizationSection) resume.getSections().get(sectionType)).getOrganizations()) {
        %>
        <dl>
            <dt> Название</dt>
            <dd><input type="text" name="<%=sectionType.name()%>" size=100 value="<%=org.getHomePage().getName()%>">
            </dd>
        </dl>
        <dl>
            <dt> Ссылка</dt>
            <dd><input type="text" name="<%=sectionType.name()%>url" size=100 value="<%=org.getHomePage().getUrl()%>">
            </dd>
        </dl>
        <br>
        <%
            for (Organization.Position pos : org.getPositions()) {
        %>
        <dl>
            <dt>Начальная дата</dt>
            <dd>
                <input type="text" name="<%=sectionType.name()%><%=counter%>startDate" size=10
                       value="<%=DateUtil.format(pos.getStartDate())%>" placeholder="MM/yyyy">
            </dd>
        </dl>
        <dl>
            <dt>Конечная дата</dt>
            <dd>
                <input type="text" name="<%=sectionType.name()%><%=counter%>endDate" size=10
                       value="<%=DateUtil.format(pos.getEndDate())%>" placeholder="MM/yyyy">
            </dd>
        </dl>
        <dl>
            <dt>Позиция</dt>
            <dd><input type="text" name="<%=sectionType.name()%><%=counter%>title" size=75
                       value="<%=pos.getTitle()%>"></dd>
        </dl>
        <dl>
            <dt>Описание</dt>
            <dd><textarea name="<%=sectionType.name()%><%=counter%>description" rows=5
                          cols=75><%=pos.getDescription()%></textarea></dd>
        </dl>
        <%
                            }
                            counter++;
                        }
                }
            }
        %>
        <button type="submit">Сохранить</button>
        <button onclick="window.history.back()">Отменить</button>
    </form>
</section>
</body>
</html>