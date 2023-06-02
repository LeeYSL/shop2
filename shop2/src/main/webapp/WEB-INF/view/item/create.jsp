<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- 서버에서 파라미터 검증.파일 업로드 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>상품등록</title> 
</head>
<body>
  <%-- form:form :유효성 검증  --%>
	 <%-- form:form.../> enctype="mulitpart/form-data" 인경우 
	                     method="post" 로 설정 됨 --%>
	 <%--http://localhost:8080/shop1/item/create
	          =>get 방식 : 화면출력
	          =>post 방식 요청 : 파일 업로드+db에 데이터 저장                   
	  --%>                    
	<form:form modelAttribute="item" action="create" 
		enctype="multipart/form-data"> 
		<h2>상품등록</h2>
		<table> 
			<tr>
				<td>상품명</td>
				  <%-- <form:input =>
                  <input type="text" name="name" id="name" value="${item.name}" 
	               form:input는 modelAttribute를 사용함? > --%>
				<td><form:input path="name" />
				<td><font color="red"><form:errors path="name" /></font></td> 
			</tr>
			<tr>
				<td>상품가격</td>
				<td><form:input path="price" />
				<td><font color="red"><form:errors path="price" /></font></td>
			</tr>
			<tr>
				<td>상품이미지</td>
				<td colspan="2"><input type="file" name="picture"></td>
			</tr>
			<tr>
				<td>상품설명</td>
				<td><form:textarea path="description" cols="20" rows="5" /></td>
				<td><font color="red"><form:errors path="description" /></font></td>
			</tr>
			<tr>
				<td colspan="3"><input type="submit" value="상품등록"> <input
					type="button" value="상품목록" onclick="location.href='list'"></td>
			</tr>
		</table></form:form></body></html>