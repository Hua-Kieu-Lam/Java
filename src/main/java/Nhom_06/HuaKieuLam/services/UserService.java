package Nhom_06.HuaKieuLam.services;

import Nhom_06.HuaKieuLam.Role;
import Nhom_06.HuaKieuLam.entities.User;
import Nhom_06.HuaKieuLam.entities.VerificationToken;
import Nhom_06.HuaKieuLam.repositories.IRoleRepository;
import Nhom_06.HuaKieuLam.repositories.IUserRepository;
import Nhom_06.HuaKieuLam.repositories.VerificationTokenRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @PersistenceContext
    private EntityManager entityManager;

    private static final int EXPIRATION = 60 * 24; // Thời hạn của token: 24 giờ

    // Save new user to database after encoding password
    public void save(@NotNull User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }

    // Set default role for user based on username
    public void setDefaultRole(String username) {
        userRepository.findByUsername(username).ifPresentOrElse(
                user -> {
                    user.getRoles().add(roleRepository.findRoleById(Role.USER.value));
                    userRepository.save(user);
                },
                () -> {
                    throw new UsernameNotFoundException("User not found");
                }
        );
    }

    // Load user details for authentication
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .accountExpired(!user.isAccountNonExpired())
                .accountLocked(!user.isAccountNonLocked())
                .credentialsExpired(!user.isCredentialsNonExpired())
                .disabled(!user.isEnabled())
                .build();
    }

    // Find user by username
    public Optional<User> findByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    // Get currently authenticated user
    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
        } else {
            throw new IllegalArgumentException("User not authenticated");
        }
    }

    // Send OTP via email
    public String sendOtpByEmail(String email) {
        String otp = generateOtp();
        emailService.sendOtp(email, otp); // Send OTP via email
        saveOrUpdateToken(email, otp); // Save OTP to database
        return otp;
    }

    // Validate OTP
    public boolean validateOtp(String email, String otp) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Optional<VerificationToken> optionalToken = tokenRepository.findByUser(user);
            return optionalToken.isPresent() && optionalToken.get().getToken().equals(otp);
        }
        return false;
    }

    // Change user password
    public void changeUserPassword(String email, String newPassword) {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                // Encode new password before saving
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String encodedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encodedPassword);

                // Tạm thời bỏ qua xác thực cho confirmPassword
                user.setConfirmPassword(null);

                entityManager.merge(user); // Merge user with new password
                entityManager.flush(); // Ensure the changes are written to the database
                log.info("Password changed successfully for user: {}", email);
            } else {
                log.error("Email does not exist in the system: {}", email);
                throw new RuntimeException("Email does not exist in the system.");
            }
        } catch (Exception e) {
            log.error("Failed to change password for email: {}", email, e);
            throw new RuntimeException("Failed to change password: " + e.getMessage(), e);
        }
    }


    // Remove OTP after it has been used
    @Transactional(rollbackFor = Exception.class) // Đảm bảo rollback khi có lỗi
    public void removeOtp(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Optional<VerificationToken> token = tokenRepository.findByUser(user);
            token.ifPresent(tokenRepository::delete); // Delete OTP token from database if exists
        } else {
            throw new UsernameNotFoundException("Email does not exist in the system.");
        }
    }


    // Save or update OTP token in database
    private void saveOrUpdateToken(String email, String newToken) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Optional<VerificationToken> existingToken = tokenRepository.findByUser(user);
            existingToken.ifPresent(tokenRepository::delete); // Delete existing token if found

            VerificationToken token = new VerificationToken(user, newToken);
            tokenRepository.save(token); // Save the new token
        } else {
            throw new UsernameNotFoundException("Email does not exist in the system.");
        }
    }

    // Generate random OTP
    private String generateOtp() {
        return UUID.randomUUID().toString().substring(0, 6); // Generate UUID and take first 6 characters
    }
}
