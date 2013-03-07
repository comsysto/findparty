<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
  Created by IntelliJ IDEA.
  User: tim.hoheisel
  Date: 07.09.12
  Time: 09:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Error Occurred</title>

    <h1>An Error occured with following stacktrace</h1>

    <br />

    <c:forEach items="${error.throwable.stackTrace}" var="element">
        <c:out value="${element}" />
    </c:forEach>


</head>
<body>

</body>
</html>