package Nhom_06.HuaKieuLam.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Meal")
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mealId")
    private int mealId;

    @ManyToOne
    @JoinColumn(name = "planMealId")
    private PlanMeal planMeal;

    @Column(name = "mealName")
    private String mealName;

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

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MealDetail> mealDetails = new ArrayList<>();

    public Meal() {
    }

    // Getters and Setters
    public int getMealId() {
        return mealId;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
    }

    public PlanMeal getPlanMeal() {
        return planMeal;
    }

    public void setPlanMeal(PlanMeal planMeal) {
        this.planMeal = planMeal;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public BigDecimal getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(BigDecimal totalCalories) {
        this.totalCalories = totalCalories;
    }

    public BigDecimal getTotalProtein() {
        return totalProtein;
    }

    public void setTotalProtein(BigDecimal totalProtein) {
        this.totalProtein = totalProtein;
    }

    public BigDecimal getTotalCarb() {
        return totalCarb;
    }

    public void setTotalCarb(BigDecimal totalCarb) {
        this.totalCarb = totalCarb;
    }

    public BigDecimal getTotalFat() {
        return totalFat;
    }

    public void setTotalFat(BigDecimal totalFat) {
        this.totalFat = totalFat;
    }

    public BigDecimal getTotalAlcohol() {
        return totalAlcohol;
    }

    public void setTotalAlcohol(BigDecimal totalAlcohol) {
        this.totalAlcohol = totalAlcohol;
    }

    public List<MealDetail> getMealDetails() {
        return mealDetails;
    }

    public void setMealDetails(List<MealDetail> mealDetails) {
        this.mealDetails = mealDetails;
    }
}
