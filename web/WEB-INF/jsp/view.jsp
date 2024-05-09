<%@ page import="come.urise.webapp.model.*" %>
<%@ page import="come.urise.webapp.util.HtmlUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <jsp:useBean id="resume" class="come.urise.webapp.model.Resume" scope="request"/>
    <jsp:include page="fragments/header.jsp"/>
    <title>${resume.fullName}</title>
</head>
<body>
<section>
    <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"> Edit </a></h2>
    <p>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<come.urise.webapp.model.ContactType, java.lang.String>"/>
            <%=contactEntry.getKey().toHtml(contactEntry.getValue())%>
            </br>
        </c:forEach>
    </p>
    <c:forEach var="sectionEntry" items="${resume.sections}">
        <c:set var="type" value="${sectionEntry.key}"/>
        <c:set var="section" value="${sectionEntry.value}"/>
        <jsp:useBean id="section" type="come.urise.webapp.model.Section"/>
        <c:choose>
            <c:when test="${sectionEntry.key == 'PERSONAL' || sectionEntry.key == 'OBJECTIVE'}">
                <h3>${type.title}</h3>
                <p><%=((TextSection)section).getContent()%></p>
            </c:when>
            <c:when test="${type == 'ACHIEVEMENT' || type == 'QUALIFICATIONS'}">
                <h3>${type.title}</h3>
                <c:forEach var="item" items="<%=((ListSection)section).getItems()%>">
                    <p>${item}</p>
                </c:forEach>
            </c:when>
            <c:when test="${type == 'EXPERIENCE' || type == 'EDUCATION'}">
                <h3>${type.title}</h3>
                <c:forEach var="organization" items="<%=((OrganizationSection)section).getOrganizations()%>">
                    <h3><a href="${organization.homePage.url}">${organization.homePage.name}</a></h3>
                    <c:forEach var="position" items="${organization.positions}">
                        <jsp:useBean id="position" type="come.urise.webapp.model.Organization.Position"/>
                        <h4>${position.title}</h4>
                        <p><%=HtmlUtil.formatDates(position)%></p>
                        <p>${position.description}</p>
                    </c:forEach>
                </c:forEach>
            </c:when>
        </c:choose>
    </c:forEach>
</section>
</body>
</html>
