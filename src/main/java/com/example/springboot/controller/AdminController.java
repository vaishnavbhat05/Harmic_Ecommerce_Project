package com.example.springboot.controller;

import com.example.springboot.DTO.CheckoutResponseDTO;
import com.example.springboot.entity.*;
import com.example.springboot.repository.ProductRepository;
import com.example.springboot.request.CheckoutRequest;
import com.example.springboot.request.StockRequest;
import com.example.springboot.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@RequestMapping("/admin")
public class AdminController {
    private CartService cartService;
    private ProductService productService;
    private WishlistService wishlistService;
    private StockService stockService;
    private ProductRepository productRepository;
    private CategoryService categoryService;
    private AboutUsService aboutUsService;
    private ContactUsService contactUsService;
    private UserService userService;
    private CheckoutService checkoutService;
    @Autowired
    public AdminController(CartService cartService, ProductService productService,WishlistService wishlistService,
                           StockService stockService, ProductRepository productRepository, CategoryService categoryService,
                           AboutUsService aboutUsService, ContactUsService contactUsService,UserService userService,
                           CheckoutService checkoutService) {
        this.cartService = cartService;
        this.productService = productService;
        this.wishlistService = wishlistService;
        this.stockService = stockService;
        this.productRepository=productRepository;
        this.categoryService=categoryService;
        this.aboutUsService=aboutUsService;
        this.contactUsService=contactUsService;
        this.userService=userService;
        this.checkoutService=checkoutService;
    }

//AboutUs

    @PostMapping("/aboutus")
    public ResponseEntity<AboutUs> saveAboutUs(@RequestBody AboutUs aboutUs) {
        AboutUs aboutUs1 = aboutUsService.saveAboutUs(aboutUs);
        return ResponseEntity.status(HttpStatus.CREATED).body(aboutUs1);
    }
    @GetMapping("/aboutus")
    public ResponseEntity<List<AboutUs>> getAllAboutUs() {
        List<AboutUs> about = aboutUsService.getAllAboutUs();
        if (!about.isEmpty()) {
            return ResponseEntity.ok(about);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
    @PutMapping("/aboutus")
    public ResponseEntity<AboutUs> updateAboutUs(@RequestBody AboutUs aboutUs) {
        AboutUs aboutUs1 = aboutUsService.saveAboutUs(aboutUs);
        return ResponseEntity.status(HttpStatus.CREATED).body(aboutUs1);
    }
    @DeleteMapping("/aboutus")
    public ResponseEntity<Void> deleteAboutUs() {
        aboutUsService.deleteAboutUs();
        return ResponseEntity.noContent().build();
    }

//ContactUs

    @PostMapping("/contactus")
    public ResponseEntity<ContactUs> saveContact(@RequestBody ContactUs contactUs) {
        ContactUs contactUs1 = contactUsService.saveContact(contactUs);
        return ResponseEntity.status(HttpStatus.CREATED).body(contactUs1);
    }
    @GetMapping("/contactus")
    public ResponseEntity<List<ContactUs>> getAllContacts() {
        List<ContactUs> contact = contactUsService.getAllContact();
        if (!contact.isEmpty()) {
            return ResponseEntity.ok(contact);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
    @PutMapping("/contactus")
    public ResponseEntity<ContactUs> updateContactUs(@RequestBody ContactUs contactUs) {
        ContactUs contactUs1 = contactUsService.saveContact(contactUs);
        return ResponseEntity.status(HttpStatus.CREATED).body(contactUs1);
    }
    @DeleteMapping("/contactus")
    public ResponseEntity<Void> deleteContact() {
        contactUsService.deleteContact();
        return ResponseEntity.noContent().build();
    }

/***-----------------------------------------------Vaishnav-----------------------------------------------------------*/

//ADMIN and USER Details

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    @GetMapping("/all/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User updates) {
        try {
            User updatedUser = userService.updateUser(id, updates);
            return ResponseEntity.ok("Account updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/all/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//Products

    @PostMapping(value="/products", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> addProduct(@RequestPart("product") String product, @RequestPart("image") MultipartFile img)
            throws IOException {
        Products addedProduct = productService.addProduct(product, img);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product has been added Successfully.");
    }

    //All Products Details with Pagination and Sorting
    //http://localhost:8080/admin/products?page=0&size=2&field=prodName
    @GetMapping("/products")
    public List<Products> getAll (@RequestParam String field, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, field);
        return productRepository.findAll(pageable).toList();
    }

    @GetMapping("/products/id/{id}")
    public ResponseEntity<Products> getProductById(@PathVariable Long id) {
        Optional<Products> productOptional = productService.getProductById(id);
        if (productOptional.isPresent()) {
            return ResponseEntity.ok(productOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Find Product By Name

    @GetMapping("/products/name/{name}")
    public ResponseEntity<Products> getProductByName(@PathVariable String name) {
        Optional<Products> productOptional = productService.getProductByName(name);
        if (productOptional.isPresent()) {
            return ResponseEntity.ok(productOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Two Product Comparison

    @GetMapping("/products/compare")
    public ResponseEntity<List<Products>> compareProducts(
            @RequestParam(value = "ids") List<Long> productIds) {
        List<Products> products = productService.getProductsByIds(productIds);
        // Sort products based on price in ascending order
        Collections.sort(products, new ProductPriceComparator());
        return ResponseEntity.ok(products);
    }

    //Product Image by using product ID

    @GetMapping("/products/image/{id}")
    public ResponseEntity<?> getProductImageById(@PathVariable Long id) throws IOException {
        byte[] imageData = productService.getProductImageById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }

    //Updating Product Details

    @PatchMapping("/products/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Products updatedProduct) {
        Products updated = productService.updateProductFields(id, updatedProduct);
        if (updated != null) {
            return ResponseEntity.ok("Product updated successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return ResponseEntity.ok().build();
    }


/***-----------------------------------------------Pawan--------------------------------------------------------------*/

//Category

    @PostMapping("/category")
    public ResponseEntity<String> addCategory(@RequestBody Category category) {
        Category createdCategory = categoryService.addCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body("Category added successfully.");
    }
    @GetMapping("/category/all")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long categoryId) {
        Optional<Category> category = categoryService.getCategoryById(categoryId);
        if (category.isPresent()) {
            return ResponseEntity.ok(category.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PatchMapping("/category/{categoryId}")
    public ResponseEntity<Category> updateCategoryPartially(
            @PathVariable Long categoryId,
            @RequestBody Map<String, Object> updates) {
        Category updatedCategory = categoryService.updateCategory(categoryId, updates);
        if (updatedCategory != null) {
            return ResponseEntity.ok(updatedCategory);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/category/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

//Checkout

    @PostMapping("/updateOrderStatus/{checkoutId}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long checkoutId, @RequestBody CheckoutRequest req) {
        String status=req.getOrderStatus();
        Checkout updatedCheckout = checkoutService.updateOrderStatus(checkoutId, status);
        if (updatedCheckout != null) {
            return ResponseEntity.ok("Order status updated successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/checkout/history/{userId}")
    public ResponseEntity<? extends Object> getCheckoutHistoryByUserId(@PathVariable Long userId) {
        List<CheckoutResponseDTO> checkoutHistory = checkoutService.getCheckoutHistoryByUserId(userId);
        return ResponseEntity.ok(checkoutHistory);
    }

    @GetMapping("/checkout/all")
    public ResponseEntity<List<Checkout>> getAllCheckouts() {
        List<Checkout> checkouts = checkoutService.getAllCheckouts();
        return ResponseEntity.ok(checkouts);
    }
    @GetMapping("/checkout/{checkoutId}")
    public ResponseEntity<Checkout> getCheckoutById(@PathVariable Long checkoutId) {
        Optional<Checkout> checkout = Optional.ofNullable(checkoutService.getCheckoutById(checkoutId));
        if (checkout.isPresent()) {
            return ResponseEntity.ok(checkout.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


/***-----------------------------------------------Sheetal------------------------------------------------------------*/

//Stock

    @PostMapping("/stock")
    public ResponseEntity<String> saveStock(@RequestBody StockRequest req) {
        Stock stock = new Stock(req);
        stock = stockService.addStock(stock);
        int quantity = stockService.availability(req.getProducts());
        String avail;
        if (quantity > 0) {
            avail = "In stock";
        } else {
            avail = "Out of stock";
        }
        Long prodId = req.getProducts().getId();
        productRepository.setAvailability(avail, prodId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Stock added successfully.");
    }
    @GetMapping("/stock")
    public ResponseEntity<List<Stock>> getAllStock() {
        List<Stock> stock = stockService.getAllStock();
        return new ResponseEntity<>(stock, HttpStatus.OK);
    }
    @GetMapping("/stock/{id}")
    public ResponseEntity<Stock> getStockById(@PathVariable Long id) {
        Optional<Stock> stock = Optional.ofNullable(stockService.findById(id));
        return stock.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PatchMapping("/stock/update/{id}")
    public ResponseEntity<?> updateStock(@PathVariable Long id, @RequestBody Stock updatedStock) {
        Stock updated = stockService.updateStock(id, updatedStock);
        if (updated != null) {
            return ResponseEntity.ok("Stock updated successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}