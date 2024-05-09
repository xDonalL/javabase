<%@ page import="come.urise.webapp.model.ContactType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <jsp:include page="fragments/header.jsp"/>
    <title>Список всех резюме</title>
</head>
<body>
<section>
    <jsp:useBean id="resume" class="come.urise.webapp.model.Resume"/>
    <a href="resume?action=add">Добавить резюме</a>
    <table>
        <tr>
            <th>Имя</th>
            <th>Email</th>
            <th></th>
            <th></th>
        </tr>
        <c:forEach var="resume" items="${resumes}">
            <tr>
                <td><a href="resume?uuid=${resume.uuid}&action=view">${resume.fullName}</a></td>
                <td>${resume.getContacts().get(ContactType.EMAIL)}</td>
                <td><a href="resume?uuid=${resume.uuid}&action=delete"> Удалить </a></td>
                <td><a href="resume?uuid=${resume.uuid}&action=edit"> Изменить </a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>
