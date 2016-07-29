<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page
	import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.LockedAccountException "%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
</head>

<body>
	<h1>MAIN</h1>
	<a href="${ctx}/user">管理用户</a>
	<a href="${ctx}/facade/TempInvCompany/search">普通用户能看</a>
	<a href="${ctx}/facade/TempPart/search">只有管理员能看</a>
	<a href="${ctx}/temp.html">新增/修改</a>
	<a href="${ctx}/logout">登出</a>
</body>
</html>
