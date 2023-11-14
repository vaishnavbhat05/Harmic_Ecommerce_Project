package com.example.springboot.controller;

import com.example.springboot.DTO.CheckoutResponseDTO;
import com.example.springboot.entity.*;
import com.example.springboot.repository.CartItemRepository;
import com.example.springboot.repository.ProductRepository;
import com.example.springboot.request.CartItemRequest;
import com.example.springboot.request.CheckoutRequest;
import com.example.springboot.service.*;
import com.example.springboot.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@PreAuthorize("hasAuthority('ROLE_USER')")
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private CartItemService cartitemService;
    private CartItemRepository cartItemRepository;
    private StockService stockService;
    private CheckoutService checkoutService;
    private CartService cartService;
    private ProductService productService;
    private WishlistService wishlistService;
    private AboutUsService aboutUsService;
    private ContactUsService contactUsService;
    private ProductRepository productRepository;
    private CategoryService categoryService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    public UserController(UserService userService, CartItemService cartitemService,
                          CartItemRepository cartItemRepository, StockService stockService,CheckoutService checkoutService,
                          CartService cartService, ProductService productService, WishlistService wishlistService,
                          AboutUsService aboutUsService,ContactUsService contactUsService,ProductRepository productRepository,
                          CategoryService categoryService) {
        this.userService = userService;
        this.cartitemService = cartitemService;
        this.cartItemRepository = cartItemRepository;
        this.stockService = stockService;
        this.checkoutService=checkoutService;
        this.cartService = cartService;
        this.productService=productService;
        this.wishlistService=wishlistService;
        this.aboutUsService=aboutUsService;
        this.contactUsService=contactUsService;
        this.productRepository=productRepository;
        this.categoryService=categoryService;
    }

//Home

    @GetMapping("/home")
    public String home() {
        return "Welcome to Harmic Shopping Cart!";
    }

//AboutUs

    @GetMapping("/aboutus")
    public ResponseEntity<List<AboutUs>> getAllAboutUs() {
        List<AboutUs> about = aboutUsService.getAllAboutUs();
        if (!about.isEmpty()) {
            return ResponseEntity.ok(about);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

//ContactUs

    @GetMapping("/contactus")
    public ResponseEntity<List<ContactUs>> getAllContacts() {
        List<ContactUs> contact = contactUsService.getAllContact();
        if (!contact.isEmpty()) {
            return ResponseEntity.ok(contact);
        } else {
            return ResponseEntity.noContent().build();
        }
    }


/***-----------------------------------------------Vaishnav-----------------------------------------------------------*/

//USER Details
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PatchMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        try {
            User updatedUser = userService.updateUser(id, (User) updates);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


//Products

    @GetMapping("/products/all")
    public ResponseEntity<List<Products>> getAllProducts() {
        List<Products> products = productService.getAllProducts();
        if (!products.isEmpty()) {
            return ResponseEntity.ok(products);
        } else {
            return ResponseEntity.noContent().build();
        }
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

    //Product with Pagination and Sorting
    //http://localhost:8080/users/products?page=0&size=2&field=prodName
    @GetMapping("/products")
    public List<Products> getAll (@RequestParam String field, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, field);
        return productRepository.findAll(pageable).toList();
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

    //Getting New Arrivals of Products

    @GetMapping("/products/newArrivals")
    public ResponseEntity<List<Products>> newProducts() {
        List<Products> products = stockService.newArrivals();
        return ResponseEntity.ok(products);
    }


//Wishlist

    @PostMapping("/wishlist")
    public ResponseEntity<String> saveWishlistItem(@RequestBody Wishlist wishlistItem) {
        Wishlist savedWishlistItem = wishlistService.saveWishlistItem(wishlistItem);
        return ResponseEntity.status(HttpStatus.CREATED).body("Wishlist item saved successfully.");
    }
    //wishlist by userId Add now
    @GetMapping("/wishlist/user/{userId}")
    public ResponseEntity<List<Wishlist>> getWishlistItemsByUserId(@PathVariable Long userId) {
        List<Wishlist> wishlistItems = wishlistService.getWishlistItemsByUserId(userId);
        if (!wishlistItems.isEmpty()) {
            return new ResponseEntity<>(wishlistItems, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/wishlist/user/{userId}")
    public ResponseEntity<Void> deleteWishlistItemsByUserId(@PathVariable Long userId) {
        wishlistService.deleteWishlistItemsByUserId(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    /***-----------------------------------------------Sheetal------------------------------------------------------------*/

//Cartitem

    @PostMapping("/cartitem")
    public ResponseEntity<?> savecartitem(@RequestBody CartItemRequest req) {
        CartItem cartItem = new CartItem(req);
        int qty=stockService.availability(req.getProducts());
        if(qty>0)
        {
            int qty1=qty-req.getQty();
            if(qty1>=0)
            {
                CartItem item=cartitemService.findByProduct(req.getProducts(),req.getCart());
                if(item==null)
                {
                    BigDecimal total = cartitemService.calcTotal(req.getProducts().getPrice(), req.getQty());
                    cartItem.setTotal(total);
                    cartitemService.addItem(cartItem);
                    stockService.updateStock(req.getProducts(), qty1);
                    return new ResponseEntity<>("Product added to cart!!", HttpStatus.CREATED);
                }
                BigDecimal tot = cartitemService.calcTotal(req.getProducts().getPrice(), req.getQty());
                item.setQty(item.getQty()+ req.getQty());
                item.setTotal(item.getTotal().add(tot));
                cartitemService.addItem(item);
                stockService.updateStock(req.getProducts(), qty1);
                return new ResponseEntity<>("Product already exists in cart!! Quantity updated", HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>("Only "+qty+" products available!!", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Product out of stock!!", HttpStatus.OK);
    }

    //Getting CartItem Details

    @GetMapping("/cartitem/{cartId}")
    public ResponseEntity<List<CartItem>> getCartitemByCartId(@PathVariable Long cartId) {
        List<CartItem> cartItems = cartitemService.getCartitemByCartId(cartId);
        return ResponseEntity.ok(cartItems);
    }
    @PatchMapping("/cartitem/updateQuantity/{id}")
    public ResponseEntity<?> updateItem(@PathVariable Long id, @RequestBody CartItem updatedItem) {
        String updated = cartitemService.updateQty(id, updatedItem);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/cartitem/{id}")
    public ResponseEntity<Void> deleteCartitemById(@PathVariable Long id) {
        cartitemService.deleteCartItemById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


/***-----------------------------------------------Pawan--------------------------------------------------------------*/

//Category

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

//Checkout

    @PostMapping("/checkout")
    public ResponseEntity<?> addCheckout(@RequestBody Checkout checkout) {
        try {
            Checkout createdCheckout = checkoutService.addCheckout(checkout);

            // Check if the total cost is above 500
            BigDecimal totalCostWithDelivery = createdCheckout.getTotalCost();
            if (totalCostWithDelivery.compareTo(BigDecimal.valueOf(500)) > 0) {
                totalCostWithDelivery = totalCostWithDelivery.add(Checkout.FIXED_DELIVERY_CHARGE);
            }

            String responseMessage;
            if (totalCostWithDelivery.compareTo(BigDecimal.valueOf(500)) < 0) {
                responseMessage = "Order placed. Total cost with delivery charge: " + totalCostWithDelivery;
            } else {
                responseMessage = "Order placed.";
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
        } catch (IllegalArgumentException e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }

    //Checkout History
    @GetMapping("/checkout/history/{userId}")
    public ResponseEntity<? extends Object> getCheckoutHistoryByUserId(@PathVariable Long userId) {
        List<CheckoutResponseDTO> checkoutHistory = checkoutService.getCheckoutHistoryByUserId(userId);
        return ResponseEntity.ok(checkoutHistory);
    }

}
