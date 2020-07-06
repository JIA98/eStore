package kr.ac.hansung.cse.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import kr.ac.hansung.cse.model.Cart;
import kr.ac.hansung.cse.model.CartItem;
import kr.ac.hansung.cse.model.Product;
import kr.ac.hansung.cse.service.CartItemService;
import kr.ac.hansung.cse.service.CartService;
import kr.ac.hansung.cse.service.ProductService;

@RestController // @Controller + @ResponseBody -> bean으로 등록
@RequestMapping("/api/cart") //api를 넣어줌으로해서 RestApi라는 것을 분명하게 해줌
public class CartRestController {

	@Autowired  // dependency 주입
	private CartService cartService;

	@Autowired
	private CartItemService cartItemService;
	
	
	@Autowired
	private ProductService productService;
	
	@RequestMapping(value = "/{cartId}", method = RequestMethod.GET)
	public ResponseEntity<Cart> getCartById(@PathVariable(value = "cartId") int cartId) {  //로그인 한 사용자 마다 카트가 제공되어야 함
		
		Cart cart = cartService.getCartById(cartId);
		return new ResponseEntity<Cart>(cart, HttpStatus.OK);
		
	}

	@RequestMapping(value = "/{cartId}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> clearCart(@PathVariable(value = "cartId") int cartId) {  // 카트안에 모든 아이템 지우기
		
		Cart cart = cartService.getCartById(cartId);
		cartItemService.removeAllCartItems(cart);

		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		
	}

	@RequestMapping(value = "/cartItem/{productId}", method = RequestMethod.PUT)
	public ResponseEntity<Void> addItem(@PathVariable(value = "productId") int productId) {  // 상품을 카트에 담는 것

		Product product = productService.getProductById(productId);
		
		//아직 회원의 정보를 받지 않았기 때문에 카드는 1개 밖에 없다.
		Cart cart = cartService.getCartById(1);  // temporary  

		// 카트에서 카트 아이템을 받아온다
		List<CartItem> cartItems = cart.getCartItems();

		// 현재 추가적으로 상품을 더 담을 수도 있어서 기존에 product에  해당되는 cartItem에 있는 product들이 존재하는지 확인한다.
		// 존재하면 1만 증가시키면 되고 그게 아닌 경우에는 아이템을 새로 만든다.
		// check if cartitem for a given product already exists
		for (int i = 0; i < cartItems.size(); i++) {  // 루프를 돌면서 주어진 productId에 대한 상품이 카트에 있는지 확인한다.
			if (product.getId() == cartItems.get(i).getProduct().getId()) {
				CartItem cartItem = cartItems.get(i);
				cartItem.setQuantity(cartItem.getQuantity() + 1);  // 있을 경우 1을 증가시키고, product totalPrice를 증가시킨다
				cartItem.setTotalPrice(product.getPrice() * cartItem.getQuantity());
				
				cartItemService.addCartItem(cartItem);

				return new ResponseEntity<Void>(HttpStatus.OK);
			}
		}

		// 그렇지 않을 경우 새롭게 아이템을 만들고 수량을 1을 증가시킨다. -> totalPrice 계산
		// create new cartItem
		CartItem cartItem = new CartItem();
		cartItem.setQuantity(1);
		cartItem.setTotalPrice(product.getPrice() * cartItem.getQuantity());
		
		// cart와 product에 대한 reference 유지
		cartItem.setProduct(product);
		cartItem.setCart(cart);
		
		// 실제로 카트 아이템  DB 저장
		cartItemService.addCartItem(cartItem);

		// bidirectional
		// 카트 쪽에서도 reference 유지
		cart.getCartItems().add(cartItem);		

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/cartItem/{productId}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> removeItem(@PathVariable(value = "productId") int productId) { 

		// 카트를 가져와서 카트아이템 제거
		Cart cart = cartService.getCartById(1);  // temporary

		CartItem cartItem = cartItemService.getCartItemByProductId(cart.getId(), productId);
		cartItemService.removeCartItem(cartItem);

		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);

	}

}

