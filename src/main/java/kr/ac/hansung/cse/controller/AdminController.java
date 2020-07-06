package kr.ac.hansung.cse.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import kr.ac.hansung.cse.model.Product;
import kr.ac.hansung.cse.service.ProductService;

@Controller
@RequestMapping("/admin")

public class AdminController {

	@Autowired
	private ProductService productService;

	@RequestMapping
	public String adminPage() {
		return "admin";
	}

	@RequestMapping("/productInventory")
	public String getProduct(Model model) {

		List<Product> products = productService.getProducts();

		model.addAttribute("products", products);

		return "productInventory"; // view's logical name
	}

	@RequestMapping(value = "/productInventory/addProduct", method = RequestMethod.GET)
	public String addProduct(Model model) {

		Product product = new Product();
		product.setCategory("컴퓨터");

		model.addAttribute("product", product);

		return "addProduct"; // 시용자로부터 입력 받기
	}

	// web form data -> object(filled with form data)
	@RequestMapping(value = "/productInventory/addProduct", method = RequestMethod.POST)
	public String addProductPost(@Valid Product product, BindingResult result, HttpServletRequest request) {
		// controller -> service -> dao

		if (result.hasErrors()) {
			System.out.println("==== Web From data has some errors ====");
			List<ObjectError> errors = result.getAllErrors();

			for (ObjectError error : errors) {
				System.out.println(error.getDefaultMessage());
			}
			return "addProduct";
		}

		MultipartFile productImage = product.getProductImage(); // 사용자가 form에서 입력한 내용이 product에 쭉 들어가서 그중에 productImage를
																// 가져옴
		// webapp부터 packaging이 일어나는데 어디에 이루어지는지 루트를 판단
		// 실제로 현재 images에 대한 full path를 가져옴
		String rootDirectory = request.getSession().getServletContext().getRealPath("/");
		Path savePath = Paths.get(rootDirectory + "\\resources\\images\\" + productImage.getOriginalFilename());

		if (productImage.isEmpty() == false) {
			System.out.println("------------------- file start -------------------");
			System.out.println("name : " + productImage.getName());
			System.out.println("filename : " + productImage.getOriginalFilename());
			System.out.println("size : " + productImage.getSize());
			System.out.println("savePath : " + savePath);
			System.out.println("------------------- file end -------------------");
		}

		// 디렉터리에 실제 파일 저장하기
		if (productImage != null && !productImage.isEmpty()) {
			try {
				productImage.transferTo(new File(savePath.toString())); // 실제로 이미지를 파일에다가 변환하여 저장을 해둔다
				// 결국 DB에 파일을 저장하지 않고 별도의 디렉토리에 저장을 하는 부분
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// 데이터 베이스에는 파일 이름만 저장한다
		// form에 입력된 이미지 파일의 이름을 product에 setting을 하고 addProduct로 저장을 한다.
		product.setImageFilename(productImage.getOriginalFilename());

		// System.out.println(product);
		productService.addProduct(product);
//		if ( ! productService.addProduct(product))
//			System.out.println("Adding PRoduct cannot be done");

		return "redirect:/admin/productInventory";  // redirect 시켜서 다시 /admin/productInventory로 가면  다시 getProduct해서 모델이 담아서 조회 가능

	}

	@RequestMapping(value = "/productInventory/deleteProduct/{id}", method = RequestMethod.GET)
	public String deleteProduct(@PathVariable int id, HttpServletRequest request) {

		Product product = productService.getProductById(id);

		String rootDirectory = request.getSession().getServletContext().getRealPath("/");
		Path savePath = Paths.get(rootDirectory + "\\resources\\images\\" + product.getImageFilename());

		if (Files.exists(savePath)) {
			try { // 파일이 존재하면 실제로 파일을 지우는 부분
				Files.delete(savePath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 데이터베이스 상에서 파일 지우기
		productService.deleteProduct(product);

//		if(!productService.deleteProduct(id))
//			System.out.println("Deleting Product cannot be done");

		return "redirect:/admin/productInventory";

	}

	@RequestMapping(value = "/productInventory/updateProduct/{id}", method = RequestMethod.GET)
	public String updateProduct(@PathVariable int id, Model model) {

		Product product = productService.getProductById(id);

		model.addAttribute("product", product);

		return "updateProduct";

	}

	@RequestMapping(value = "/productInventory/updateProduct", method = RequestMethod.POST)
	public String updateProductPost(@Valid Product product, BindingResult result, HttpServletRequest request) {
		// controller -> service -> dao

		if (result.hasErrors()) {
			System.out.println("==== Web From data has some errors ====");
			List<ObjectError> errors = result.getAllErrors();

			for (ObjectError error : errors) {
				System.out.println(error.getDefaultMessage());
			}
			return "addProduct";
		}

		MultipartFile productImage = product.getProductImage();
		// 사용자가 form에서 입력한 내용이 product에 쭉 들어가서 그중에 productImage를 가져옴
		// webapp부터 packaging이 일어나는데 어디에 이루어지는지 루트를 판단
		// 실제로 현재 images에 대한 full path를 가져옴
		String rootDirectory = request.getSession().getServletContext().getRealPath("/");
		Path savePath = Paths.get(rootDirectory + "\\resources\\images\\" + productImage.getOriginalFilename());

		if (productImage.isEmpty() == false) {
			System.out.println("------------------- file start -------------------");
			System.out.println("name : " + productImage.getName());
			System.out.println("filename : " + productImage.getOriginalFilename());
			System.out.println("size : " + productImage.getSize());
			System.out.println("savePath : " + savePath);
			System.out.println("------------------- file end -------------------");
		}

		// 디렉터리에 실제 파일 저장하기
		if (productImage != null && !productImage.isEmpty()) {
			try {
				productImage.transferTo(new File(savePath.toString())); // 실제로 이미지를 파일에다가 변환하여 저장을 해둔다
				// 결국 DB에 파일을 저장하지 않고 별도의 디렉토리에 저장을 하는 부분
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// 데이터 베이스에는 파일 이름만 저장한다
		// form에 입력된 이미지 파일의 이름을 product에 setting을 하고 addProduct로 저장을 한다.
		product.setImageFilename(productImage.getOriginalFilename());

		// System.out.println(product);
		productService.updateProduct(product);
//		if ( ! productService.updateProduct(product))
//			System.out.println("Updating PRoduct cannot be done");

		return "redirect:/admin/productInventory";

	}
}
