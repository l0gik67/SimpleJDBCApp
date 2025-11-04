<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>tickets</title>
</head>
<body>
<h1>Купленные билеты на полет ${requestScope.flightId}</h1>
<ul>
  <c:forEach var="ticket" items="${requestScope.tickets}">
    <li>
      ${ticket.seatNumber()}
    </li>
  </c:forEach>
</ul>
</body>
</html>
