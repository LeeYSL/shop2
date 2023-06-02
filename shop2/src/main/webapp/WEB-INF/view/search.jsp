<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${title}찾기</title>
<script type="text/javascript">
	function sendClose() {
		opener.document.loginform.userid.value='${result}';
		self.close();
	}
</script>
</head>
<body>
	<table>
		<tr>
			<th>${title}:</th>
			<td>${result}</td>
		</tr>
		<tr>
			<td colspan="2"><c:if test="${title=='아이디'}">
					<input type="button" value="아이디 전송" onclick="sendClose()">
				</c:if> <c:if test="${title=='비밀번호'}">
					<input type="button" value="닫기" onclick="self.close()">
				</c:if>
	</table>
</body>
</html>