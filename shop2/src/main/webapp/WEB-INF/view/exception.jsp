<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true"%>
    <!-- isErrorPage="true" : exection 내장객체 전달됨. -->
<script>
  alert("${exception.message}") //CartEmptyException.getMessage() 메서드 호출
  location.href="${exception.url}"//CartEmptyException.getUrl() 메서드 호출
</script>  