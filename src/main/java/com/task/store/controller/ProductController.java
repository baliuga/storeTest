package com.task.store.controller;

import com.task.store.helper.PdfGenerator;
import com.task.store.model.Product;
import com.task.store.service.ProductServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.util.List;

@Api(value = "Product API", description = "API for managing products")
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    ProductServiceImpl productService;

    @ApiOperation(value = "View a list of available products", response = Product.class, responseContainer = "List")
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<Product>> findAllProducts() {
        return new ResponseEntity<>(productService.findAllProducts(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get product entity by id", response = Product.class)
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Product> findProduct(@ApiParam(value = "Product id", required = true) @PathVariable Long id) {
        return new ResponseEntity<>(productService.findProductById(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Create new product entity", response = Product.class)
    @PostMapping
    @ResponseBody
    public ResponseEntity<Product> createProduct(@ApiParam(value = "Product entity", required = true) @Valid @RequestBody Product product) {
        return new ResponseEntity<>(productService.createProduct(product), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update existing or create new product entity", response = Product.class)
    @PutMapping
    @ResponseBody
    public ResponseEntity<Product> updateProduct(@ApiParam(value = "Product entity", required = true) @Valid @RequestBody Product product) {
        return new ResponseEntity<>(productService.updateProduct(product), HttpStatus.OK);
    }

    @ApiOperation(value = "Delete product entity by id", response = Product.class, responseContainer = "List")
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteProduct(@ApiParam(value = "Product id", required = true) @PathVariable Long id) {
        productService.deleteProductBuId(id);
        return new ResponseEntity<>("Product with id = " + id + " was deleted.", HttpStatus.OK);
    }

    @ApiOperation(value = "Get PDF with all available products", response = InputStreamResource.class)
    @GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getProductsPdf() {
        List<Product> products = productService.findAllProducts();
        ByteArrayInputStream bis = PdfGenerator.productPdf(products);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=products.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<String> customExceptionHandler(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
