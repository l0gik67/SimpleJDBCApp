<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>Список перелетво:</h1>
<ul>
    <c:forEach var="flight" items="${requestScope.flights}">
        <li>
            <a href="${pageContext.request.contextPath}/tickets?flightId=${flight.id()}">${flight.description()}</a><br>
        </li>
    </c:forEach>
</ul>
</body>
</html>
