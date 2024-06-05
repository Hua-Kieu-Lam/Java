package Nhom_06.HuaKieuLam.controllers;

import Nhom_06.HuaKieuLam.entities.Category;
import Nhom_06.HuaKieuLam.services.CategoryService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public String showAllCategories(@NonNull Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "category/list";
    }
    @GetMapping("/add")
    public String addCategory(@NonNull Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("categories",
                categoryService.getAllCategories());
        return "category/add";
    }
    @PostMapping("/add")
    public String addCategory(
            @Validated @ModelAttribute("category") Category category,
            @NonNull BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            model.addAttribute("categories",
                    categoryService.getAllCategories());
            return "category/add";
        }
        categoryService.addCategory(category);
        return "redirect:/categories";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable long id) {
        categoryService.getCategoryById(id)
                .ifPresentOrElse(
                        category -> categoryService.deleteCategoryById(id),
                        () -> {
                            throw new IllegalArgumentException("Category not found");
                        });
        return "redirect:/categories";
    }

    @GetMapping("/edit/{id}")
    public String editCategoryForm(@NonNull Model model, @PathVariable long id) {
        var category = categoryService.getCategoryById(id);
        model.addAttribute("category", category.orElseThrow(() -> new IllegalArgumentException("Category not found")));
        return "category/edit";
    }

    @PostMapping("/edit")
    public String editCategory(@Validated @ModelAttribute("category") Category category,
                              @NonNull BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            model.addAttribute("categories",
                    categoryService.getAllCategories());
            return "category/edit";
        }
        categoryService.updateCategory(category);
        return "redirect:/categories";
    }
}
