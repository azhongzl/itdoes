<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Hi Jalen, this is a temp html</title>
</head>
<body>
	<form action="${ctx}/facade/TempPart/post" method="post">
		<table>
			<tr>
				<td>TempPart POST</td>
			</tr>
			<tr>
				<td>barCode:</td>
				<td><input type="text" name="barCode" value="barCode" /></td>
			</tr>
			<tr>
				<td>registerDate</td>
				<td><input type="text" name="registerDate" value="" /></td>
			</tr>
			<tr>
				<td><input type="submit" value="Submit Part"></td>
			</tr>
		</table>
	</form>

	<form action="#" method="post">
		<table>
			<tr>
				<td>TempPart PUT</td>
			</tr>
			<tr>
				<td>partId:</td>
				<td><input type="text" id="partIdPut" name="partId" value="" /></td>
			</tr>
			<tr>
				<td>barCode:</td>
				<td><input type="text" name="barCode" value="barCode" /></td>
			</tr>
			<tr>
				<td><input type="submit" value="Submit Part"
					onclick="this.form.action='${ctx}/facade/TempPart/put' + '/' + document.getElementById('partIdPut').value"></td>
			</tr>
		</table>
	</form>

	<br />

	<form action="${ctx}/facade/TempInvCompany/post" method="post">
		<table>
			<tr>
				<td>TempInvCompany POST</td>
			</tr>
			<tr>
				<td>comment:</td>
				<td><input type="text" name="comment" value="comment" /></td>
			</tr>
			<tr>
				<td>partId:</td>
				<td><input type="text" name="partId" value="3" /></td>
			</tr>
			<tr>
				<td><input type="submit" value="Submit InvCompany"></td>
			</tr>
		</table>
	</form>

	<form action="#" method="post">
		<table>
			<tr>
				<td>TempInvCompany PUT</td>
			</tr>
			<tr>
				<td>invCompanyId:</td>
				<td><input type="text" id="invCompanyIdPut" name="invCompanyId"
					value="" /></td>
			</tr>
			<tr>
				<td>comment:</td>
				<td><input type="text" name="comment" value="comment" /></td>
			</tr>
			<tr>
				<td><input type="submit" value="Submit InvCompany"
					onclick="this.form.action='${ctx}/facade/TempInvCompany/put' + '/' + document.getElementById('invCompanyIdPut').value"></td>
			</tr>
		</table>
	</form>
	<a href="${ctx}/">Back</a>
</body>
</html>
