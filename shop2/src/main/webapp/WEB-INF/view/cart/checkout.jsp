<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>주문 전 상품 목록 보기</title>
</head>
<body>
	<h2>배송지 정보</h2>
	<table>
		<tr>  
			<td width="30%">주문아이디</td>
			<td width="70%">${sessionScope.loginUser.userid}</td>
		</tr>
		<tr>
			<td width="30%">이름</td>
			<td width="70%">${sessionScope.loginUser.username}</td>
		</tr>
		<tr>
			<td width="30%">우편번호</td>
			<td width="70%">${sessionScope.loginUser.postcode}</td>
		</tr>
		<tr>
			<td width="30%">주소</td>
			<td width="70%">${sessionScope.loginUser.address}</td>
		</tr>
		<tr>
			<td width="30%">전화번호</td>
			<td width="70%">${sessionScope.loginUser.phoneno}</td>
		</tr>
	</table>
	<h2>구매 상품</h2>
	<table>
		<tr>
			<th>상품명</th>
			<th>가격</th>
			<th>수량</th>
			<th>합계</th>
		</tr>
		<c:forEach items="${sessionScope.CART.itemSetList}" var="itemSet"
			varStatus="stat">
			<tr>
				<td>${itemSet.item.name}</td>
				<td>${itemSet.item.price}</td>
				<td>${itemSet.quantity}</td>
				<td>${itemSet.item.price *itemSet.quantity}</td>
			</tr>
		</c:forEach>
		<tr>
			<td colspan="4" align="right">총 구입 금액 :
				${sessionScope.CART.total}원</td>
		</tr>
		<tr>
			<td colspan="4"><a href="javascript:kakaopay()">결제하기</a>&nbsp; <a
				href="../item/list">상품목록</a>&nbsp;</td>
		</tr>
	</table>
	<%--결제하기 모듈 : 개발자 문서 --%>
	<script type="text/javascript" src="https://cdn.iamport.kr/js/iamport.payment-1.1.8.js"></script>
	<script  type="text/javascript">
	   let IMP = window.IMP
	   IMP.init("imp01555276") //가맹점 식별코드
	   
	   function kakaopay() {
		   $.ajax("kakao",{
			   success : function(json) {
				   iamPay(json)
				
			}
		   })
		
	}
	function iamPay(json) {
		IMP.request_pay({
			pg :"kakaopay", // 상점구분. 카카오페이
			pay_method : "card", //결제 방식 : 카드
			merchant_uid : json.merchant_uid, //주문번호 : 주문별로 유일한 값으로 필요함 usrrid-session id 값
			name : json.name, //주문상품명. ex) 사과 외 3건
			amount : json.amount, //전체 주문 금액
			buyer_email :"dudtjs960630@naver.com", //주문자 이메일 테스트
			buyer_name : json.buyer_name, //주문자 성명
			buyer_tel : json.buyer_tel, //주문자 전화번호
			buyer_addr : json.buyer_addr, //주문자 주소
			buyer_postcode : json.buyer_postcode //주문자 우편번호
		},function(rsp){
			if(rsp.success) {
				let msg = "결제가 완료 되었습니다."
				msg += "\n:고유:ID : " + rsp.imp_uid
				msg += "\n:상점ID : " + rsp.merchant_uid
				msg += "\n:결제금액 :  " + rsp.paid_amount
				alert(msg)
				location.href="end"
					
			}else {
				alert("결제에 실패 했습니다.:"  + rsp.error_msg)
			}
			
		})
		
	}
	
	</script>
</body>
</html>