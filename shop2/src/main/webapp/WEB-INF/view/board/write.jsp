<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp"%>
<%--
   http://localhost:8080/shop1/board/write 요청 시 write.jsp 화면 출력
 --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 작성</title>
</head>
<body>
	<form:form modelAttribute="board" action="write"
		enctype="multipart/form-data" name="f">
		<table class="w3-table">
			<tr>
				<th>글쓴이</th>
				<td><form:input path="writer" class="w3-input" /> <font
					color="red"><form:errors path="writer" /></font></td>
			</tr>
			<tr>
				<th>비밀번호</th>
				<td><form:password path="pass" class="w3-input" /> <font
					color="red"><form:errors path="pass" /></font></td>
			</tr>
			<tr>
				<th>제목</th>
				<td><form:input path="title" class="w3-input" /> <font
					color="red"><form:errors path="title" /></font></td>
			</tr>
			<tr>
				<th>내용</th>
				<td><form:textarea path="content" row="15" cols="80" /> <font
					color="red"><form:errors path="content" /></font></td>
			</tr>
			<script>
				CKEDITOR.replace("content", {
					filebrowserImageUploadUrl : "imgupload"
				})
			</script>
			<tr>
				<th>첨부파일</th>
				<td><input type="file" name="file1"></td>
			</tr>
			<tr>
				<td colspan="2" class="w3-center"><a
					href="javascript:document.f.submit()">[게시글 등록]</a> <a
					href="list?boardid=${boardid}">[게시글 목록]</a></td>
			</tr>
		</table>
	</form:form>
</body>
</html>