
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class LoginAndEditProductTest {

    private static final String CHROME_DRIVER_PATH = "C:/Program Files/chromedriver-win64/chromedriver.exe";
    private static WebDriver driver;
    private static WebDriverWait wait;

    public static void main(String[] args) {
        // Thiết lập driver cho Chrome
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));

        try {
            login();
            String[][] testCases = {
                    //1
                    {"Khoai tây", "1005", "1", "11", "1", "1", "1", "1", "1", "C:\\Users\\PC-LAM\\Pictures\\fruit & vegetable\\khoai_tay.jpg"},
                    //2
                    {"abcde fghij ABCDE FGHIJ 12345 67890 abcde fghij AB", "1000", "1", "11", "1", "1", "1", "1", "1", "C:\\Users\\PC-LAM\\Pictures\\fruit & vegetable\\khoai_tay.jpg"},
                    //3
                    {"Khoai tây", "1000", "1", "10", "1", "1", "1", "1", "1", "C:\\Users\\PC-LAM\\Pictures\\fruit & vegetable\\khoai_tay.jpg"},
                    //4
                    {"Khoai tây", "1001", "1", "10", "1", "1", "1", "1", "1", "C:\\Users\\PC-LAM\\Pictures\\fruit & vegetable\\khoai_tay.jpg"},

                    //5
                    {"Khoai tây", "1001", "0", "10", "1", "1", "1", "1", "1", "C:\\Users\\PC-LAM\\Pictures\\fruit & vegetable\\khoai_tay.jpg"},

                    //6
                    {"Khoai tây", "2000", "1", "10", "1", "1", "1", "1", "1", "C:\\Users\\PC-LAM\\Pictures\\fruit & vegetable\\khoai_tay.jpg"},

                    //7
                    {"Khoai tây", "1001", "1", "11", "1", "1", "1", "1", "1", "C:\\Users\\PC-LAM\\Pictures\\fruit & vegetable\\khoai_tay.jpg"},

                    //8
                    {"Khoai tây", "3000", "1", "10", "1", "1", "1", "1", "1", "C:\\Users\\PC-LAM\\Pictures\\fruit & vegetable\\khoai_tay.jpg"},

                    //9
                    {"Khoai tây", "2000", "1", "10", "0", "0", "0", "0", "0", "C:\\Users\\PC-LAM\\Pictures\\fruit & vegetable\\khoai_tay.jpg"},

                    //10
                    {"Khoai tây", "2000", "1", "10", "1", "", "", "", "", "C:\\Users\\PC-LAM\\Pictures\\fruit & vegetable\\khoai_tay.jpg"},
//                    //11

                    //12
                    {"", "2000", "1", "10", "1", "1", "1", "1", "1", "C:\\Users\\PC-LAM\\Pictures\\fruit & vegetable\\khoai_tay.jpg"},

                    //13
                    {"Khoai tây", "999", "1", "10", "1", "1", "1", "1", "1", "C:\\Users\\PC-LAM\\Pictures\\fruit & vegetable\\khoai_tay.jpg"},

                    //14
                    {"Khoai tây", "-1", "1", "10", "1", "1", "1", "1", "1", "C:\\Users\\PC-LAM\\Pictures\\fruit & vegetable\\khoai_tay.jpg"},

                    //15
                    {"Khoai tây", "a", "1", "10", "1", "", "", "", "", "C:\\Users\\PC-LAM\\Pictures\\fruit & vegetable\\khoai_tay.jpg"},

                    //16
                    {"Khoai tây", "1000.5", "1", "10", "1", "1", "1", "1", "1", "C:\\Users\\PC-LAM\\Pictures\\fruit & vegetable\\khoai_tay.jpg"},

                    //17
                    {"Khoai tây", "", "1", "10", "1", "1", "1", "1", "1", "C:\\Users\\PC-LAM\\Pictures\\fruit & vegetable\\khoai_tay.jpg"},

                    //18
                    {"Khoai tây", "1000", "-1", "10", "1", "1", "1", "1", "1", "C:\\Users\\PC-LAM\\Pictures\\fruit & vegetable\\khoai_tay.jpg"},

                    //19
                    {"Khoai tây", "1000", "1.5", "10", "1", "1", "1", "1", "1", "C:\\Users\\PC-LAM\\Pictures\\fruit & vegetable\\khoai_tay.jpg"},

                    //20
                    {"Khoai tây", "1000", "a", "10", "1", "1", "1", "1", "1", "C:\\Users\\PC-LAM\\Pictures\\fruit & vegetable\\khoai_tay.jpg"},

                    //21
                    {"Khoai tây", "1000", "1", "Thức ăn nhanh", "1", "1", "1", "1", "1", "C:\\Users\\PC-LAM\\Pictures\\fruit & vegetable\\khoai_tay.jpg"},

                    //22
                    {"Khoai tây", "1001", "1", "10", "-0.9", "-0.9", "-0.9", "-0.9", "-0.9", "C:\\Users\\PC-LAM\\Pictures\\fruit & vegetable\\khoai_tay.jpg"},

                    //23
                    {"Khoai tây", "1001", "1", "10", "a", "a", "a", "a", "a", "C:\\Users\\PC-LAM\\Pictures\\fruit & vegetable\\khoai_tay.jpg"},

                    //24
                    {"Khoai tây", "1000", "1", "10", "", "1", "1", "1", "1", "C:\\Users\\PC-LAM\\Pictures\\fruit & vegetable\\khoai_tay.jpg"},

                    //25
                    {"Khoai tây", "1000", "1", "10", "1", "1", "1", "1", "1", "D:\\BAI HOC\\NAM4\\NAM4_HK1A\\Kiem thu va dam bao chat luong PM\\Cuoi Ky\\test.txt"},

                    //11
                    {"abcde fghij ABCDE FGHIJ 12345 67890 abcde fghij ABCD", "2000", "1", "10", "1", "1", "1", "1", "1", "C:\\Users\\PC-LAM\\Pictures\\fruit & vegetable\\khoai_tay.jpg"},


            };

            for (int i = 0; i < testCases.length; i++) {
                System.out.println("Running Test Case " + (i + 1));
                try {
                    editProduct(testCases[i]);
                } catch (Exception e) {
                    System.out.println("Test Case " + (i + 1) + " failed: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            System.out.println("Error occurred during login or main process: " + e.getMessage());
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    private static void login() {
        try {
            driver.get("http://localhost:8080/login");

            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
            usernameField.sendKeys("lam");

            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
            passwordField.sendKeys("123456");

            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']")));
            loginButton.click();
            driver.get("http://localhost:8080/products");
            wait.until(ExpectedConditions.urlContains("/products"));
        } catch (Exception e) {
            System.out.println("Error during login: " + e.getMessage());
        }
    }

    private static void editProduct(String[] testCase) {
        List<String> errorMessages = new ArrayList<>();

        try {
            WebElement editButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@href, '/edit/')]")));
            editButton.click();

            // Kiểm tra Title
            if (isEmptyField(testCase[0])) {
                errorMessages.add("Test không thành công! Title không được để trống.");
            } else {
                fillField(By.id("title"), testCase[0]); // Title
            }

            // Kiểm tra Price
            if (isEmptyField(testCase[1]) || !isGreaterThanOrEqual(testCase[1], 1000)) {
                errorMessages.add("Test không thành công! Price phải lớn hơn hoặc bằng 1000.");
            } else {
                fillField(By.id("price"), testCase[1]);
            }

            // Kiểm tra Quantity
            if (isEmptyField(testCase[2]) || !isGreaterThanOrEqual(testCase[2], 0)) {
                errorMessages.add("Test không thành công! Quantity phải lớn hơn hoặc bằng 0.");
            } else {
                fillField(By.id("quantity"), testCase[2]);
            }

            selectCategory(testCase[3]);

            // Kiểm tra Calories
            if (isEmptyField(testCase[4]) || !isGreaterThanOrEqual(testCase[4], 0)) {
                errorMessages.add("Test không thành công! Calories phải lớn hơn hoặc bằng 0.");
            } else {
                fillField(By.id("caloriesPerGram"), testCase[4]);
            }

            // Kiểm tra Protein
            if (!isEmptyField(testCase[5]) && !isGreaterThanOrEqual(testCase[5], 0)) {
                errorMessages.add("Test không thành công! Protein phải lớn hơn hoặc bằng 0.");
            } else {
                fillField(By.id("protein"), testCase[5]);
            }

            // Kiểm tra Carbs
            if (!isEmptyField(testCase[6]) && !isGreaterThanOrEqual(testCase[6], 0)) {
                errorMessages.add("Test không thành công! Carbs phải lớn hơn hoặc bằng 0.");
            } else {
                fillField(By.id("carbs"), testCase[6]);
            }

            // Kiểm tra Fat
            if (!isEmptyField(testCase[7]) && !isGreaterThanOrEqual(testCase[7], 0)) {
                errorMessages.add("Test không thành công! Fat phải lớn hơn hoặc bằng 0.");
            } else {
                fillField(By.id("fat"), testCase[7]);
            }

            // Kiểm tra Alcohol
            if (!isEmptyField(testCase[8]) && !isGreaterThanOrEqual(testCase[8], 0)) {
                errorMessages.add("Test không thành công! Alcohol phải lớn hơn hoặc bằng 0.");
            } else {
                fillField(By.id("alcohol"), testCase[8]);
            }

            uploadImage(testCase[9]);

            WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='submit']")));
            saveButton.click();

            wait.until(ExpectedConditions.urlContains("/products"));
            String currentUrl = driver.getCurrentUrl();
            if (!currentUrl.contains("/products")) {
                errorMessages.add("Test không thành công!");
            }

        } catch (Exception e) {
            System.out.println("General error in editProduct method: " + e.getMessage());
        }

        if (!errorMessages.isEmpty()) {
            for (String message : errorMessages) {
                System.out.println(message);
            }
        } else {
            System.out.println("Chỉnh sửa sản phẩm thành công!");
        }
    }


    private static boolean isEmptyField(String value) {
        return value == null || value.isEmpty();
    }

    private static boolean isGreaterThanOrEqual(String value, int minValue) {
        try {
            return Integer.parseInt(value) >= minValue;
        } catch (NumberFormatException e) {
            return false;
        }
    }

        private static void fillField(By locator, String value) {
        try {
            WebElement field = wait.until(ExpectedConditions.elementToBeClickable(locator));
            field.clear();
            field.sendKeys(value);
        } catch (Exception e) {
            System.out.println("Error setting field: " + locator + " - " + e.getMessage());
        }
    }

    private static void selectCategory(String categoryId) {
        try {
            WebElement categoryDropdown = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("category")));
            Select categorySelect = new Select(categoryDropdown);
            categorySelect.selectByValue(categoryId);
        } catch (NoSuchElementException e) {
            System.out.println("Error: Cannot locate option with value: " + categoryId);
        }
    }

    private static void uploadImage(String imagePath) {
        try {
            WebElement fileUploadField = driver.findElement(By.id("image"));
            fileUploadField.sendKeys(imagePath);
        } catch (Exception e) {
            System.out.println("Error uploading Image: " + e.getMessage());
        }
    }
}
