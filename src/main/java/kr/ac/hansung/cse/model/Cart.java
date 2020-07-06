package kr.ac.hansung.cse.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

@Entity //데이터베이스 상에 테이블로 만들어 준다
public class Cart implements Serializable{

	// object version 정보 확인하기
	private static final long serialVersionUID = -7383420736137539222L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) // auto increment와 같은 효과
	private int id;
	
	//하나의 카트에 여러개의 product -> onetomany
	@OneToMany(mappedBy="cart", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	// cart는 cartItem에 있는 cart를 가져온다. 나중에 foreign key 별도로 테이블상에 필드를 만들지 말고 cartItem에 있는 cart로 접근해라
	// oneToMany의 fetch 기본 타입은 many쪽은 LAZY, 근데 EAGER로 줌 -> cart로 담기는 것은 많지 않아 코딩을 좀 더 편하게 하기 위해 줌
	// cart를 읽으면 자동적으로 cartItem까지 읽어온다. 
	private List<CartItem> cartItems = new ArrayList<CartItem>();
	
	private double grandTotal;
}
