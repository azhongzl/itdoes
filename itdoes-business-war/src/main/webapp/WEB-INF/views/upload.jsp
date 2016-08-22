<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Upload</title>
</head>
<body>
	<h1>Please upload a file</h1>
	<form method="post" action="${ctx}/upload"
		enctype="multipart/form-data">
		Name: <input type="text" name="name" /> <br /> File: <input
			type="file" name="file" multiple="multiple" /> <br /> <input
			type="submit" />
	</form>
</body>
</html>