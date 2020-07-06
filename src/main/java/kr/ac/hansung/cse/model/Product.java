package kr.ac.hansung.cse.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity; //jpa에 관련된것
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity  //hibernate에 의해 table이 된다
@Table(name="product")
public class Product implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -567117648902464025L;

	@Id  //primary key
	@GeneratedValue(strategy=GenerationType.IDENTITY) //IDENTITY는 auto_increment가 체크되어 자동으로 1씩 증가한다
	//default는 auto -> table형태로 table을 사용해서 primary key가 생성 (sequence number를 저장)
	@Column(name="product_id", nullable = false, updatable = false) //추가적으로 null이 되거나 update가 되면 안된다
	private int id;
	
	@NotEmpty(message="The product name must not be null")
	private String name;
	
	private String category;
	
	@Min(value=0, message="The product price must not be less than zero")
	private int price;
	
	@NotEmpty(message="The product manufacturer must not be null")
	private String manufacturer;
	
	@Min(value=0, message="The product unitInStock must not be less than zero")
	private int unitInStock;
	
	private String description;
	
	@Transient //DB에 저장할때 빼고 저장한다
	private MultipartFile productImage; //파일에 대한 메타 정보도 함께 담겨있다.
	
	private String imageFilename; //실제 사진은 빠지고 사진의 이름만 저장이 된다.
}  //hibernate -> jpa + native (추가적으로 hibernate의 고유한 기능)

