package com.itdoes.common.dozer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.dozer.Mapping;
import org.junit.Test;

/**
 * @author Jalen Zhong
 */
public class DozerMapperTest {
	@Test
	public void map() {
		ProductDto productDto = new ProductDto();
		productDto.setName("car");
		productDto.setPrice("200");

		PartDto partDto = new PartDto();
		partDto.setName("door");
		partDto.setProduct(productDto);

		productDto.setParts(new PartDto[] { partDto });

		Product product = DozerMapper.map(productDto, Product.class);
		assertThat(product.getProductName()).isEqualTo(productDto.getName());
		assertThat(product.getPrice()).isEqualTo(Double.valueOf(productDto.getPrice()).doubleValue());
		assertThat(product.getParts().get(0).getName()).isEqualTo(partDto.getName());
		assertThat(product.getParts().get(0).getProduct().getProductName()).isEqualTo(productDto.getName());

		ProductDto productDto2 = DozerMapper.map(product, ProductDto.class);
		assertThat(productDto2.getName()).isEqualTo(product.getProductName());
		assertThat(productDto2.getPrice()).isEqualTo(String.valueOf(product.getPrice()));
		assertThat(productDto2.getParts()[0].getName()).isEqualTo(product.getParts().get(0).getName());
	}

	@Test
	public void copy() {
		ProductDto productDto = new ProductDto();
		productDto.setName("car");
		productDto.setPrice("200");

		PartDto partDto = new PartDto();
		partDto.setName("door");
		partDto.setProduct(productDto);

		productDto.setParts(new PartDto[] { partDto });

		Product product = new Product();
		product.setProductName("horse");
		product.setPrice(20D);
		product.setWeight(new Double(20));

		DozerMapper.copy(productDto, product);
		assertThat(product.getProductName()).isEqualTo(productDto.getName());
		assertThat(product.getPrice()).isEqualTo(Double.valueOf(productDto.getPrice()).doubleValue());
		assertThat(product.getWeight()).isEqualTo(20D);
		assertThat(product.getParts().get(0).getProduct().getProductName()).isEqualTo(productDto.getName());
	}

	public static class Product {
		private String productName;
		private Double price;
		private List<Part> parts;

		// Not exist property in Dto
		private Double weight;

		public String getProductName() {
			return productName;
		}

		public void setProductName(String productName) {
			this.productName = productName;
		}

		public Double getPrice() {
			return price;
		}

		public void setPrice(Double price) {
			this.price = price;
		}

		public List<Part> getParts() {
			return parts;
		}

		public void setParts(List<Part> parts) {
			this.parts = parts;
		}

		public Double getWeight() {
			return weight;
		}

		public void setWeight(Double weight) {
			this.weight = weight;
		}
	}

	public static class Part {
		private Product product;
		private String name;

		public Product getProduct() {
			return product;
		}

		public void setProduct(Product product) {
			this.product = product;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	public static class ProductDto {
		// Different property name, "productName" in Po
		@Mapping("productName")
		private String name;
		// String in Dto and Double in Po
		private String price;
		// Array in Dto and List in Po
		private PartDto[] parts;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPrice() {
			return price;
		}

		public void setPrice(String price) {
			this.price = price;
		}

		public PartDto[] getParts() {
			return parts;
		}

		public void setParts(PartDto[] parts) {
			this.parts = parts;
		}
	}

	public static class PartDto {
		private ProductDto product;
		private String name;

		public ProductDto getProduct() {
			return product;
		}

		public void setProduct(ProductDto product) {
			this.product = product;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
