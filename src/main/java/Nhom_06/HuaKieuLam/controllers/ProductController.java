package Nhom_06.HuaKieuLam.controllers;

import Nhom_06.HuaKieuLam.daos.Item;
import Nhom_06.HuaKieuLam.entities.Category;
import Nhom_06.HuaKieuLam.entities.Product;
import Nhom_06.HuaKieuLam.services.CartService;
import Nhom_06.HuaKieuLam.services.CategoryService;
import Nhom_06.HuaKieuLam.services.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final CartService cartService;
    private final ResourceLoader resourceLoader;

    @GetMapping
    public String showAllProducts(@NonNull Model model,
                               @RequestParam(defaultValue = "0")
                               Integer pageNo,
                               @RequestParam(defaultValue = "20")
                               Integer pageSize,
                               @RequestParam(defaultValue = "id")
                               String sortBy) {
        model.addAttribute("products", productService.getAllProducts(pageNo,
                pageSize, sortBy));
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages",
                productService.getAllProducts(pageNo, pageSize, sortBy).size() / pageSize);
        model.addAttribute("categories",
                categoryService.getAllCategories());
        return "product/list";
    }

    @GetMapping("/add")
    public String addProductForm(@NonNull Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories",
                categoryService.getAllCategories());
        return "product/add";
    }

@PostMapping("/add")
public String addProduct(
        @Validated @ModelAttribute("product") Product product,
        @NonNull BindingResult bindingResult,
        @RequestParam("image") MultipartFile multipartFile,
        Model model) {

    if (bindingResult.hasErrors()) {
        var errors = bindingResult.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toArray(String[]::new);
        model.addAttribute("errors", errors);
        model.addAttribute("categories",
                categoryService.getAllCategories());
        return "product/add";
    }

    try {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String uploadDir = "src/main/resources/static/images/";
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Sử dụng try-with-resources để đảm bảo rằng InputStream được đóng đúng cách
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
        product.setImageUrl("/images/" + fileName);
    } catch (IOException e) {
        e.printStackTrace();
        model.addAttribute("error", "Có lỗi xảy ra.");
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product/add";
    }

    productService.addProduct(product);
    return "redirect:/products";
}


    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable long id) {
        productService.getProductById(id)
                .ifPresentOrElse(
                        product -> productService.deleteProductById(id),
                        () -> {
                            throw new IllegalArgumentException("Product not found");
                        });
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String editProductForm(@NonNull Model model, @PathVariable long id) {
        var product = productService.getProductById(id);
        model.addAttribute("product", product.orElseThrow(() -> new IllegalArgumentException("Product not found")));
        model.addAttribute("categories",
                categoryService.getAllCategories());
        return "product/edit";
    }

//    @PostMapping("/edit")
//    public String editProduct(@Validated @ModelAttribute("product") Product product,
//                           @NonNull BindingResult bindingResult,
//                           Model model) {
//        if (bindingResult.hasErrors()) {
//            var errors = bindingResult.getAllErrors()
//                    .stream()
//                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
//                    .toArray(String[]::new);
//            model.addAttribute("errors", errors);
//            model.addAttribute("categories",
//                    categoryService.getAllCategories());
//            return "product/edit";
//        }
//        productService.updateProduct(product);
//        return "redirect:/products";
//    }
@PostMapping("/edit")
public String editProduct(
        @Validated @ModelAttribute("product") Product product,
        @NonNull BindingResult bindingResult,
        @RequestParam("image") MultipartFile multipartFile,
        Model model) {

    if (bindingResult.hasErrors()) {
        var errors = bindingResult.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toArray(String[]::new);
        model.addAttribute("errors", errors);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product/edit";
    }

    try {
        if (!multipartFile.isEmpty()) {
            // Xử lý lưu ảnh mới nếu có tải lên
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String uploadDir = "src/main/resources/static/images/";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = multipartFile.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }
            product.setImageUrl("/images/" + fileName);
        } else {
            // Nếu không tải lên ảnh mới, giữ lại ảnh cũ
            Product existingProduct = productService.getProductById(product.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));
            product.setImageUrl(existingProduct.getImageUrl());
        }
    } catch (IOException e) {
        e.printStackTrace();
        model.addAttribute("error", "Có lỗi xảy ra.");
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product/edit";
    }

    productService.updateProduct(product);
    return "redirect:/products";
}


    @PostMapping("/add-to-cart")
    public String addToCart(HttpSession session,
                            @RequestParam long id,
                            @RequestParam String name,
                            @RequestParam double price,
                            @RequestParam(defaultValue = "1") int
                                    quantity) {
        var cart = cartService.getCart(session);
        cart.addItems(new Item(id, name, price, quantity));
        cartService.updateCart(session, cart);
        return "redirect:/products";
    }

    @GetMapping("/search")
    public String searchProduct(
            @NonNull Model model,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        model.addAttribute("products", productService.searchProduct(keyword));
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages",
                productService
                        .getAllProducts(pageNo, pageSize, sortBy)
                        .size() / pageSize);
        model.addAttribute("categories",
                categoryService.getAllCategories());
        return "product/list";
    }
}
