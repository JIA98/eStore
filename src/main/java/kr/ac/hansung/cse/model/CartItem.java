package kr.ac.hansung.cse.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

@Entity
public class CartItem implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7296960050350583877L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) // auto increment와 같은 효과
	private int id;
	
	@ManyToOne
	@JoinColumn(name="cartId") // 나중에 database상에서 foreign key로 사용된다
	@JsonIgnore
	private Cart cart;
	
	@ManyToOne
	@JoinColumn(name="productId")
	private Product product;
	
	private int quantity;
	
	private double totalPrice;

}
