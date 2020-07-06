package kr.ac.hansung.cse.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart")
public class CartController {

	@RequestMapping
	public String getCart (Model model) {
		
		// 실제로는 사용자마다 카트를 할당해야한다.
		
		model.addAttribute("cartId", 1);
		
		return "cart";
	}
}
