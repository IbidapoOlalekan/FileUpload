package com.springapi.uploading;

import com.springapi.uploading.model.Product;
import com.springapi.uploading.repo.ProductRepo;
import com.springapi.uploading.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UploadingApplicationTests {
	@Autowired
	private ProductRepo productRepo;
	@Autowired
	private ProductService productService;

	@Test
	void contextLoads() {
	}

	@Test
	void testSaveAttachment() throws Exception{
		MockMultipartFile mockFile = new MockMultipartFile(
				"file","test.txt","test/plain","Hello, world!".getBytes()
		);
		Product product = productService.saveAttachment(mockFile);
		assertNotNull(product.getId());
		assertEquals("test.txt",product.getFileName());
		assertEquals("test/plain",product.getFileType());
	}

	@Test
	void testSaveFiles() throws Exception{
		MockMultipartFile mockFileOne = new MockMultipartFile(
				"file","test1.pdf","text/plain","Hello, world!".getBytes()
		);
		MockMultipartFile mockFileTwo = new MockMultipartFile(
				"file","test2.txt","text/plain","Goodbye, world!".getBytes()
		);
		productService.saveFiles(new MultipartFile[]{mockFileOne,mockFileTwo});
		List<Product> products = productService.getAllFiles();

		System.out.println("Saved Files: ");
		for (Product product : products) {
			System.out.println(product.getFileName());
		}
		assertEquals(2,products.size());
		assertEquals("test1.pdf",products.get(0).getFileName());
		assertEquals("test2.txt",products.get(1).getFileName());
		assertEquals("text/plain",products.get(0).getFileType());
	}

	@BeforeEach
	void setUp(){
		productRepo.deleteAll();
	}

	@Test
	void testSaveAttachmentWithInvalidName(){
		MockMultipartFile mockFile = new MockMultipartFile(
				"file","../test.txt","text/plain","Hello,world!".getBytes()
		);
		assertThrows(Exception.class,()-> productService.saveAttachment(mockFile));
	}

	@Test
	void testSaveAttachmentTooLarge(){
		byte[] bytes = new byte[1024 * 1024 * 10];
		MockMultipartFile mockFile = new MockMultipartFile(
				"file","test.txt","text/plain",bytes
		);
		assertThrows(Exception.class, ()-> productService.saveAttachment(mockFile));
	}

}
