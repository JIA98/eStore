package kr.ac.hansung.cse.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import kr.ac.hansung.cse.model.Product;

@Repository // component-scan을 통해서 @를 찾아 bean으로 등록이 이루어진다.
@Transactional // method들이 전부 transaction으로 수행된다 (begin tx -> commit 이 자동으로 들어감 (AOP기능))
public class ProductDao {

	// jdbc -> spring jdbc -> hibernate
	@Autowired // bean으로 singleton 객체가 sessionFactory에 주입이 되다.
	private SessionFactory sessionFactory;

	public Product getProductById(int id) {
		
		Session session = sessionFactory.getCurrentSession(); // session하나를 가지고 온다.
		Product product = (Product) session.get(Product.class, id);
		// 첫번째 인자에 해당되는 테이블을 찾고, 매핑되는 테이블의 아이디를 바탕으로 해당되는 레코드를 찾아서 객체를 넘겨준다.

		return product;
	}

	public List<Product> getProducts() {
		
		Session session = sessionFactory.getCurrentSession(); // session을 하나 가지고와서
		String hql = "from Product";

		Query<Product> query = session.createQuery(hql, Product.class); // 쿼리를 만들고
		List<Product> productList = query.getResultList(); // 모든 레코드를 조회한다 리스트를 받아서 리턴

		return productList;

	}

	public void addProduct(Product product) {
		
		Session session = sessionFactory.getCurrentSession(); // session을르 얻어와
		session.saveOrUpdate(product); // product를 저장한다
		session.flush();

	}

	public void deleteProduct(Product product) {
		
		Session session = sessionFactory.getCurrentSession();
		session.delete(product);
		session.flush();

	}

	public void updateProduct(Product product) {
		
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(product);
		session.flush();

	}

//	private JdbcTemplate jdbcTemplate;
//
//	@Autowired // spring Container에게 dataSource라는 것을 주입을 시켜준다.
//	// dataSource는 singleton이라서 하나밖에 없기 때문에 바로 찾아서 주입가능
//
//	public void setDataSource(DataSource dataSource) {
//		// setter메서드가 불릴 때 dataSource가 의존성 주입을 통해 주입이 이루어지게 된다.
//		jdbcTemplate = new JdbcTemplate(dataSource);
//		// jdbcTamplate에 대한 객체를 만들어서 데이터가 저장된다
//		// 이것으로 실제로 데이터베이스에 접근한다.
//	}
//
//	public List<Product> getProducts() {
//
//		String sqlStatement = "select * from product";
//		return jdbcTemplate.query(sqlStatement, new RowMapper<Product>() {
//			// 두번째 인자가 익명클래스로 sqlStatement로 조회한 레코드를 객체로 매핑을 해준다.
//
//			@Override
//			public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
//
//				Product product = new Product(); // 현재 데이터베이스에서 레코드를 가져와서 mapRow가 3번 불려서 product를 만들어서 return해준다.
//
//				product.setId(rs.getInt("id")); // rs에서 id부분을 읽어서 product의 id부분을 setting해줌
//				product.setName(rs.getString("name"));
//				product.setCategory(rs.getString("category"));
//				product.setPrice(rs.getInt("price"));
//				product.setManufacturer(rs.getString("manufacturer"));
//				product.setUnitInStock(rs.getInt("unitInStock"));
//				product.setDescription(rs.getString("description"));
//
//				return product;
//
//			}
//
//		});
//
//	}
//
//	public boolean addProduct(Product product) {
//
//		int id = product.getId();
//		String name = product.getName();
//		String category = product.getCategory();
//		int price = product.getPrice();
//		String manufacturer = product.getManufacturer();
//		int unitInStock = product.getUnitInStock();
//		String description = product.getDescription();
//
//		String sqlStatement = "insert into product (id, name, category, price, manufacturer, unitInStock, description ) values (?, ?, ?, ?, ?, ?, ?) ";
//		// id는 auto_increment -> 설정할 필요없음
//
//		return (jdbcTemplate.update(sqlStatement,
//				new Object[] { id, name, category, price, manufacturer, unitInStock, description }) == 1); // 1이면 true
//
//	}
//
//	public boolean deleteProduct(int id) {
//		String sqlStatement = "delete from product where id=?";
//
//		return (jdbcTemplate.update(sqlStatement, new Object[] { id }) == 1);
//	}
//
//	public Product getProductById(int id) {
//		String sqlStatement = "select * from product where id = ?";
//		return jdbcTemplate.queryForObject(sqlStatement, new Object[] {id}, new RowMapper<Product>() {
//
//			@Override
//			public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
//
//				Product product = new Product(); // 현재 데이터베이스에서 레코드를 가져와서 mapRow가 3번 불려서 product를 만들어서 return해준다.
//
//				product.setId(rs.getInt("id")); // rs에서 id부분을 읽어서 product의 id부분을 setting해줌
//				product.setName(rs.getString("name"));
//				product.setCategory(rs.getString("category"));
//				product.setPrice(rs.getInt("price"));
//				product.setManufacturer(rs.getString("manufacturer"));
//				product.setUnitInStock(rs.getInt("unitInStock"));
//				product.setDescription(rs.getString("description"));
//
//				return product;
//
//			}
//
//		});
//	}
//
//	public boolean updateProduct(Product product) {
//		
//		int id = product.getId();
//		String name = product.getName();
//		String category = product.getCategory();
//		int price = product.getPrice();
//		String manufacturer = product.getManufacturer();
//		int unitInStock = product.getUnitInStock();
//		String description = product.getDescription();
//
//		String sqlStatement = "update product set name=?, category=?, price=?, manufacturer=?, unitInStock=?, description=? where id=? ";
//		// id는 auto_increment -> 설정할 필요없음
//
//		return (jdbcTemplate.update(sqlStatement,
//				new Object[] { name, category, price, manufacturer, unitInStock, description, id }) == 1); // 1이면 true
//
//	
//	}
}
