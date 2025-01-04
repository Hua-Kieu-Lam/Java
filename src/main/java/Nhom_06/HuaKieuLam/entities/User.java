package Nhom_06.HuaKieuLam.entities;

import Nhom_06.HuaKieuLam.repositories.ValidationGroups;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", length = 50, unique = true)
    @NotBlank(message = "Username is required", groups = ValidationGroups.OnCreate.class)
    @Size(min = 1, max = 50, message = "Username must be between 1 and 50 characters", groups = ValidationGroups.OnCreate.class)
    private String username;

    @Column(name = "password", length = 250)
    @NotBlank(message = "Password is required", groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    private String password;

    @Transient
    @NotBlank(message = "Confirm Password is required", groups = ValidationGroups.OnCreate.class)
    private String confirmPassword;

    @Column(name = "email", length = 50, unique = true)
    @Email(message = "Email should be valid", groups = ValidationGroups.OnCreate.class)
    private String email;

    @Column(name = "phone", length = 10)
    @Length(min = 10, max = 10, message = "Phone must be 10 characters", groups = ValidationGroups.OnCreate.class)
    @Pattern(regexp = "^[0-9]*$", message = "Phone must be number", groups = ValidationGroups.OnCreate.class)
    private String phone;

    @Column(name = "provider", length = 50)
    private String provider;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "date_of_birth")
        private Date dateOfBirth;

    @Column(name = "status", length = 255)
    private String status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Cart> carts = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> userRoles = this.getRoles();
        return userRoles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
