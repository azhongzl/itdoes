<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
	<title>403 - Authorization</title>
</head>

<body>
	<h2>403 - Authorization.</h2>
	<p><a href="<c:url value="/"/>">Back</a></p>
</body>
</html>
