package Nhom_06.HuaKieuLam.controllers;

import Nhom_06.HuaKieuLam.entities.User;
import Nhom_06.HuaKieuLam.repositories.IUserRepository;
import Nhom_06.HuaKieuLam.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Autowired
    private IUserRepository userRepository;


    @GetMapping("/login")
    public String login() {
        return "users/login";
    }
    @GetMapping("/register")
    public String register(@NotNull Model model) {
        model.addAttribute("user", new User()); // Thêm một đối tượng User mới vào model
        return "users/register";
    }
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user, // Validate đối tượng User
                           @NotNull BindingResult bindingResult, // Kết quả của quá trình validate
                           Model model) {
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.user", "Passwords do not match");
        }
        if (bindingResult.hasErrors()) { // Kiểm tra nếu có lỗi validate
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "users/register"; // Trả về lại view "register" nếu có lỗi
        }
        userService.save(user); // Lưu người dùng vào cơ sở dữ liệu
        userService.setDefaultRole(user.getUsername()); // Gán vai trò mặc định cho người dùng
        return "redirect:/login"; // Chuyển hướng người dùng tới trang "login"
    }

    @GetMapping("/oauth2/loginSuccess")
    public String getLoginInfo(OAuth2AuthenticationToken authentication) {
        String email = authentication.getPrincipal().getAttribute("email");
        User user = userService.findByUsername(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setUsername(email);
            newUser.setEmail(email);
            userService.save(newUser);
            return newUser;
        });
        return "redirect:/";
    }

    // Đổi mật khẩu

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm(Model model) {
        model.addAttribute("email", new String());
        return "users/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPasswordForm(@RequestParam("email") String email, Model model) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            // Gửi OTP qua email
            String otp = userService.sendOtpByEmail(email);

            if (otp != null) {
                model.addAttribute("email", email);
                return "redirect:/reset-password?email=" + email; // Chuyển hướng đến trang đặt lại mật khẩu với email
            } else {
                model.addAttribute("error", "Không gửi được OTP.");
            }
        } else {
            model.addAttribute("error", "Không tìm thấy email.");
        }
        return "users/forgot-password";
    }


    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("email") String email, Model model) {
        model.addAttribute("email", email);
        return "users/reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPasswordForm(@RequestParam("email") String email,
                                           @RequestParam("otp") String otp,
                                           @RequestParam("newPassword") String newPassword,
                                           @RequestParam("confirmPassword") String confirmPassword,
                                           Model model) {
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu không khớp.");
            model.addAttribute("email", email); // Thêm email vào mô hình để hiển thị lại
            return "users/reset-password";
        }

        try {
            boolean isValidOtp = userService.validateOtp(email, otp);
            if (!isValidOtp) {
                model.addAttribute("error", "OTP không hợp lệ.");
                model.addAttribute("email", email); // Thêm email vào mô hình để hiển thị lại
                return "users/reset-password";
            }

            // Xác thực thành công OTP, tiến hành đổi mật khẩu
            userService.changeUserPassword(email, newPassword);
            userService.removeOtp(email);
            model.addAttribute("message", "Mật khẩu đã được đặt lại thành công.");
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", "Đã xảy ra lỗi: " + e.getMessage());
            model.addAttribute("email", email); // Thêm email vào mô hình để hiển thị lại
            return "users/reset-password";
        }
    }

    //profile
    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.getUserProfile(username);
        model.addAttribute("userProfile", user); // Đặt tên biến là "userProfile" để tham chiếu trong template
        return "users/profile";
    }
    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute("user") User updatedUser) {
        userService.updateUserProfile(updatedUser);
        return "redirect:/profile";
    }

}