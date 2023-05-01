package com.springapi.uploading;

import com.springapi.uploading.repo.ProductRepo;
import com.springapi.uploading.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UploadingApplicationTests {
	@Autowired
	private ProductRepo productRepo;
	@Autowired
	private ProductService productService;

	@Test
	void contextLoads() {
	}

}
