package com.example.springboot.service;

import com.example.springboot.entity.Category;
import com.example.springboot.entity.Products;
import com.example.springboot.entity.Stock;
import com.example.springboot.repository.CategoryRepository;
import com.example.springboot.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductService {
    private ProductRepository productRepository;

    private CategoryRepository categoryRepository;

    private final String FOLDER_PATH="C:/Users/Ganesh/IdeaProjects/harmic/photos/";

    public ProductService(ProductRepository productRepository,CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public Optional<Products> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public List<Products> getAllProducts() {
        return productRepository.findAll();
    }

    public byte[] getProductImageById(Long id) throws IOException {
        Optional<Products> products = productRepository.findById(id);
        String filePath=products.get().getImage();
        byte[] images = Files.readAllBytes(new File(filePath).toPath());
        return images;
    }

    public Optional<Products> getProductByName(String name) {

        return productRepository.findByProdName(name);
    }

    public Products addProduct(String product, MultipartFile img) throws IOException {
        String filePath=FOLDER_PATH+img.getOriginalFilename();
        Products productJson=new Products();
        try{
            ObjectMapper objectMapper=new ObjectMapper();
            productJson=objectMapper.readValue(product,Products.class);
        } catch (IOException err) {
            System.out.printf("Error", err.toString());
        }
        productJson.setImage(filePath);
        productJson.setAvail("Out Of Stock");
        Products prod=productRepository.save(productJson);
        img.transferTo(new File(filePath));
        return prod;
    }
    public List<Products> getProductsByIds(List<Long> productIds) {
        return productRepository.findByIdIn(productIds);
    }
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    public Products updateProductFields(Long id, Products updatedProduct) {
        Optional<Products> product = productRepository.findById(id);
        if (product.isPresent()) {
            Products existingProduct = product.get();
            if (updatedProduct.getProdName() != null) {
                existingProduct.setProdName(updatedProduct.getProdName());
            }
            if (updatedProduct.getCategory() != null) {
                existingProduct.setCategory(updatedProduct.getCategory());
            }
            if (updatedProduct.getPrice() != null) {
                existingProduct.setPrice(updatedProduct.getPrice());
            }
            if (updatedProduct.getDesc() != null) {
                existingProduct.setDesc(updatedProduct.getDesc());
            }
            if (updatedProduct.getImage() != null) {
                existingProduct.setImage(updatedProduct.getImage());
            }
            return productRepository.save(existingProduct);
        }
        else {
            return null;
        }
    }
}
