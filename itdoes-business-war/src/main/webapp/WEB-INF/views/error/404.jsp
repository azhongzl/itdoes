<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
	//Set return code 200，avoid browser uses its native error page
	//response.setStatus(200);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>404 - Page Not Found</title>
</head>

<body>
	<h2>404 - Page Not Found.</h2>
	<p>
		<a href="<c:url value="/"/>">Back</a>
	</p>
</body>
</html>
