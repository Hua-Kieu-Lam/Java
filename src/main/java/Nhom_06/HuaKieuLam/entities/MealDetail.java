package Nhom_06.HuaKieuLam.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "MealDetail")
public class MealDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mealDetailId")
    private int mealDetailId;

    @ManyToOne
    @JoinColumn(name = "mealId")
    private Meal meal;

    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;

    @Column(name = "gram")
    private int gram;

    @Column(name = "totalCalories")
    private BigDecimal totalCalories;

    @Column(name = "totalProtein")
    private BigDecimal totalProtein;

    @Column(name = "totalCarb")
    private BigDecimal totalCarb;

    @Column(name = "totalFat")
    private BigDecimal totalFat;

    @Column(name = "totalAlcohol")
    private BigDecimal totalAlcohol;

//    // Getters and Setters
//    public int getMealDetailId() {
//        return mealDetailId;
//    }
//
//    public void setMealDetailId(int mealDetailId) {
//        this.mealDetailId = mealDetailId;
//    }
//
//    public Meal getMeal() {
//        return meal;
//    }
//
//    public void setMeal(Meal meal) {
//        this.meal = meal;
//    }
//
//    public Product getProduct() {
//        return product;
//    }
//
//    public void setProduct(Product product) {
//        this.product = product;
//    }
//
//    public int getGram() {
//        return gram;
//    }
//
//    public void setGram(int gram) {
//        this.gram = gram;
//    }
//
//    public BigDecimal getTotalCalories() {
//        return totalCalories;
//    }
//
//    public void setTotalCalories(BigDecimal totalCalories) {
//        this.totalCalories = totalCalories;
//    }
//
//    public BigDecimal getTotalProtein() {
//        return totalProtein;
//    }
//
//    public void setTotalProtein(BigDecimal totalProtein) {
//        this.totalProtein = totalProtein;
//    }
//
//    public BigDecimal getTotalCarb() {
//        return totalCarb;
//    }
//
//    public void setTotalCarb(BigDecimal totalCarb) {
//        this.totalCarb = totalCarb;
//    }
//
//    public BigDecimal getTotalFat() {
//        return totalFat;
//    }
//
//    public void setTotalFat(BigDecimal totalFat) {
//        this.totalFat = totalFat;
//    }
//
//    public BigDecimal getTotalAlcohol() {
//        return totalAlcohol;
//    }
//
//    public void setTotalAlcohol(BigDecimal totalAlcohol) {
//        this.totalAlcohol = totalAlcohol;
//    }
}

