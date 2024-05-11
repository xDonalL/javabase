<%@ page import="come.urise.webapp.model.*" %>
<%@ page import="come.urise.webapp.util.HtmlUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/styles.css">
    <link rel="stylesheet" href="css/view-resume-styles.css">
    <link rel="stylesheet" href="css/light.css">
    <jsp:useBean id="resume" class="come.urise.webapp.model.Resume" scope="request"/>
    <title>${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<div class="scrollable-panel">
    <div class="form-wrapper">
        <div class="full-name">${resume.fullName}
            <a class="no-underline-anchor" href="resume?uuid=${resume.uuid}&action=edit">
                <img src="img/edit.svg" alt="">
            </a>
        </div>
        <div class="contacts">
            <c:forEach var="contactEntry" items="${resume.contacts}">
                <jsp:useBean id="contactEntry"
                             type="java.util.Map.Entry<come.urise.webapp.model.ContactType, java.lang.String>"/>
                <div><%=HtmlUtil.toFormatedContacts(contactEntry.getKey(), contactEntry.getValue())%>
                </div>
            </c:forEach>
        </div>

        <div class="spacer"></div>
        <c:forEach var="sectionEntry" items="${resume.sections}">
            <c:set var="type" value="${sectionEntry.key}"/>
            <c:set var="section" value="${sectionEntry.value}"/>
            <jsp:useBean id="section" type="come.urise.webapp.model.Section"/>
            <div class="section">${type.title}</div>
            <c:choose>
                <c:when test="${type=='OBJECTIVE'}">
                    <div class="position"><%=((TextSection) section).getContent()%>
                    </div>
                </c:when>
                <c:when test="${type=='PERSONAL'}">
                    <div class="qualities"><%=((TextSection) section).getContent()%>
                    </div>
                </c:when>
                <c:when test="${type == 'ACHIEVEMENT' || type == 'QUALIFICATIONS'}">
                    <ul class="list">
                        <c:forEach var="item" items="<%=((ListSection)section).getItems()%>">
                            <li>${item}</li>
                        </c:forEach>
                    </ul>
                </c:when>
                <c:when test="${type == 'EXPERIENCE' || type == 'EDUCATION'}">
                    <c:forEach var="org" items="<%=((OrganizationSection) section).getOrganizations()%>">
                        <div class="section-wrapper">
                            <c:choose>
                                <c:when test="${empty org.homePage.url}">
                                    <div class="job-name">${org.homePage.name}</div>
                                </c:when>
                                <c:otherwise>
                                    <div class="job-name">
                                        <a class="contact-link"
                                                             href="${org.homePage.url}">${org.homePage.name}</a>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                            <c:forEach var="position" items="${org.positions}">
                                <jsp:useBean id="position" type="come.urise.webapp.model.Organization.Position"/>
                                <div class="period-position">
                                    <div class="period"><%=HtmlUtil.formatDates(position)%></div>
                                    <div class="position">${position.title}</div>
                                </div>
                                <c:choose>
                                    <c:when test="${empty position.description}">
                                    </c:when>
                                    <c:otherwise>
                                        <div class="description">${position.description}</div>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </div>
                    </c:forEach>
                </c:when>
            </c:choose>
        </c:forEach>
        <div class="footer-spacer"></div>
    </div>
</div>
</body>
</html>
