package logic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor // 매개변수 없는 생성자
public class SaleItem {
	private int saleid; //주문번호
	private int seq;  //주문상품 번호
	private int itemid; //상품 아이디
	private int quantity; //주문상품 수량
	private Item item; //상품 아이디에 해당하는 상품 정보
	public SaleItem(int saleid, int seq, ItemSet itemSet) {
		this.saleid = saleid; 
		this.seq = seq;
		this.item = itemSet.getItem();
		this.itemid = itemSet.getItem().getId(); //상품id
		this.quantity = itemSet.getQuantity();   //주문수량
	}

}
