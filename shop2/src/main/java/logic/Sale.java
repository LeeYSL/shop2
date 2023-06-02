package logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Sale {
   private int saleid;
   private String userid;
   private Date saledate;
   private User user;
   private List<SaleItem> itemlist = new ArrayList<>(); //주문 상품 목록
   public int getTotal() { //주문 상품 전체 금액 리턴
	   int sum = 0;
	// (상품가격*주문수량)의 전체 합 
/*	   
	   
	  //for구문 - 알고리즘상 좋다.
	   * 
	   for(SaleItem s : itemlist) {
		   sum += s.getItem().hashCode() * s.getQuantity();
	   }
	return sum;
*/
	   //stream 방식 -- 속도가 빠름
	   
	 return  itemlist.stream() 
		/*stream의 자료형은 saleitem이다. 정수형이 아님.
		 * saleitem을 정수형 으로 바꾸기 위해 mapToInt 사용
	     */
	     .mapToInt(s->s.getItem().getPrice() * s.getQuantity()).sum();
      
    }
public int getSaleid() {
	return saleid;
} 
public void setSaleid(int saleid) {
	this.saleid = saleid;
}
public String getUserid() {
	return userid;
}
public void setUserid(String userid) {
	this.userid = userid;
}
public Date getSaledate() {
	return saledate;
}
public void setSaledate(Date saledate) {
	this.saledate = saledate;
}
public User getUser() {
	return user;
}
public void setUser(User user) {
	this.user = user;
}
public List<SaleItem> getItemlist() {
	return itemlist;
}
public void setItemlist(List<SaleItem> itemlist) {
	this.itemlist = itemlist;
}
@Override
public String toString() {
	return "Sale [saleid=" + saleid + ", userid=" + userid + ", saledate=" + saledate + ", user=" + user + "]";
}
 	

   
}
