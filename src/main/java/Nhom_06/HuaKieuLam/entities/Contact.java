package Nhom_06.HuaKieuLam.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "contact")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contactId")
    private Long contactId;

    @Column(name = "name")
    @NotBlank(message = "Please enter your name")
    private String name;

    @Column(name = "email")
    @NotBlank(message = "Please enter your email")
    @Email(message = "Please enter a valid email address")
    private String email;

    @Column(name = "subject")
    @NotBlank(message = "Please enter a subject")
    private String subject;

    @Column(name = "phone")
    @NotBlank(message = "Please enter your phone number")
    private String phone;

    @Column(name = "message")
    @NotBlank(message = "Please enter your message")
    private String message;
}
