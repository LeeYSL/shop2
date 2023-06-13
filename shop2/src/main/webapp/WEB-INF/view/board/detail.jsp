<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp"%>
<%--
  Controller,shopSerive, BoardDao 구현
    1. num 파라미터에 해당하는 게시믈 정보를 board 객체 전달.
        serivce.getBoard(num)
        boardDao.selectOne(num)
    
    2. 조회수 증가
       boardDao.addReadcnt(num)    

 --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시물 상세 보기</title>
<style type="text/css">
.leftcol {
	text-align: left;
	vertical-align: top;
}

.lefttoptable {
	height: 250px;
	border-width: 0px;
	text-align: left;
	vertical-align: top;
	padding: 0px;
}
</style>
</head>
<body>
	<table class="w3-table-all">
		<tr>
			<td colspan="2">${boardName}</td>
		</tr>
		<tr>
			<td width="15%">글쓴이</td>
			<td width="85%" class="leftcol">${board.writer}</td>
		</tr>
		<tr>
			<td>제목</td>
			<td class="leftcol">${board.title}</td>
		</tr>
		<tr>
			<td>내용</td>
			<td class="leftcol">
				<table class="lefttoptable">
					<tr>
						<td class="leftcol lefttoptable">${board.content}</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>첨부파일</td>
			<td>&nbsp; <c:if test="${!empty board.fileurl}">
					<a href="file/${board.fileurl}">${board.fileurl}</a>
				</c:if></td>
		</tr>
		<tr>
			<td colspan="2"><a href="reply?num=${board.num}">[답변]</a> <a
				href="update?num=${board.num}">[수정]</a> <a
				href="delete?num=${board.num}">[삭제]</a> <a
				href="list?num=${board.num}">[게시물목록]</a></td>
		</tr>
	</table>
	<span id="comment"></span>
	<form:form modelAttribute="comment" action="comment" method="post"
		name="commForm">
		<form:hidden path="num" value="${board.num}" />
		<div class="w3-row">
			<div class="w3-col s2 w3-center">
				<p>
					<form:input path="writer" class="w3-input w3-boarder" placeholder="작성자"/>
					<font color="red"><form:errors path="writer" /></font>
				</p>
			</div>
			<div class="w3-col s2 w3-center">
				<p>
					<form:password path="pass" class="w3-input w3-boarder" placeholder="비밀번호"/>
					<font color="red"><form:errors path="pass" /></font>
				</p>
			</div>
			<div class="w3-col s7 w3-center">
				<p>
					<form:input path="content" class="w3-input w3-board"  placeholder="댓글내용"/>
					<font color="red"><form:errors path="content" /></font>
				</p>
			</div>
			<div class="w3-col s1 w3-center">
				<p>
					<button type="submit" class="w3-btn w3-board" >댓글등록</button>
				</p>
			</div>
		</div>
	</form:form>
	<div class="w3-container">
		<table class="w3-table-all">
			<c:forEach var="c" items="${commlist}" varStatus="stat">
				<tr>
					<td>${c.seq}</td>
					<td>${c.writer}</td>
					<td>${c.content}</td>
					<td><fmt:formatDate value="${c.regdate}"
							pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td class="w3-right">
							<form action="commdel" method="post" name="commdel${stat.index}">
							 <input type="hidden" name="num" value="${param.num}">
							 <input type="hidden" name="seq" value="${c.seq}">
							 <input type="password" name="pass" placeholder="비밀번호">
							 <a class="w3-btn w3-board w3-blue" href="javascript:document.commdel${stat.index}.submit()">삭제</a>
							 
							</form></td>
				</tr>
			</c:forEach>
		</table>
	</div>
	<script type="text/javascript">
	function comment_insert() {
	  let data = {num:document.commForm.num.value,
			       writer:document.commForm.writer.value,}
	}
	
	</script>
</body>
</html>