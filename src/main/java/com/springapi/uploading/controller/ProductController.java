package com.springapi.uploading.controller;

import com.springapi.uploading.model.Product;
import com.springapi.uploading.service.ProductService;
import com.springapi.uploading.service.ResponseClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/files")
public class ProductController {
    @Autowired
    private ProductService fileService;

    //for uploading the SINGLE file to the database
    @PostMapping("/single/base")
    public ResponseClass uploadFile(@RequestParam("file")MultipartFile file) throws Exception{
        Product attachment = null;
        String downloadUrl = "";
        attachment = fileService.saveAttachment(file);
        downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(attachment.getId())
                .toUriString();

        return new ResponseClass(attachment.getFileName(),
                downloadUrl,
                file.getContentType(),
                file.getSize());
    }

    //For Uploading Mutiple Files to the databases
    @PostMapping("/multiple/base")
    public List<ResponseClass> uploadMutipleFiles(@RequestParam("files") MultipartFile[] files) throws Exception{
        List<ResponseClass> responseList = new ArrayList<>();
        for (MultipartFile file: files){
            Product attachment = fileService.saveAttachment(file);
            String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/download/")
                    .path(attachment.getId())
                    .toUriString();
            ResponseClass response = new ResponseClass(attachment.getFileName(),
                    downloadUrl,
                    file.getContentType(),
                    file.getSize());
            responseList.add(response);
        }
        return  responseList;
    }

    //for retrieving all the files uploaded
    @GetMapping("/all")
    public ResponseEntity<List<ResponseClass>> getAllFiles(){
        List<Product> products = fileService.getAllFiles();
        List<ResponseClass> responseClasses = products.stream().map(product -> {
            String downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/download/")
                    .path(product.getId())
                    .toUriString();
            return new ResponseClass(product.getFileName(),
                    downloadURL,
                    product.getFileType(),
                    product.getData().length);
        }).collect(Collectors.toList());

        return ResponseEntity.ok().body(responseClasses);
    }
}
