<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
</head>

<body>
	<h1>
		Hello
		<shiro:principal property="username" />
	</h1>
	<a href="${ctx}/user">管理用户</a>
	<a href="${ctx}/facade/TempInvCompany/search">普通用户能看</a>
	<a href="${ctx}/facade/TempPart/search">只有管理员能看</a>
	<a href="${ctx}/edit">新增/修改</a>
	<a href="${ctx}/logout">登出</a>
</body>
</html>
