<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title>用户管理</title>
</head>

<body>
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success">
			<button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>

	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>登录名</th>
				<th>用户名</th>
				<th>状态</th>
				<th>管理</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${users}" var="user">
				<tr>
					<td><a href="${ctx}/user/update/${user.username}">${user.username}</a></td>
					<td>${user.username}</td>
					<td>${user.status}</td>
					<td><shiro:hasPermission name="TempPart:*">
							<a href="${ctx}/user/delete/${user.username}">删除</a>
						</shiro:hasPermission></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>
