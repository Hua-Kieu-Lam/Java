package Nhom_06.HuaKieuLam.controllers;

import Nhom_06.HuaKieuLam.entities.Contact;
import Nhom_06.HuaKieuLam.repositories.ContactRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/contact")
public class ContactController {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ContactRepository contactRepository;

    @Value("${spring.mail.from}")
    private String fromEmail;

    @GetMapping
    public String showContactForm(Model model) {
        model.addAttribute("contactForm", new Contact());
        return "contact/contact"; // Ensure this matches the Thymeleaf template name
    }

    @PostMapping("/send")
    public String sendEmail(@ModelAttribute("contactForm") @Valid Contact contactForm,
                            BindingResult bindingResult,
                            Model model) {
        if (bindingResult.hasErrors()) {
            return "contact/contact"; // Ensure this matches the Thymeleaf template name
        }

        try {
            // Save to database
            contactRepository.save(contactForm);

            // Send email
            String toEmail = "hotroewa@gmail.com";

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject(contactForm.getSubject());
            message.setText("Name: " + contactForm.getName() + "\nEmail: " + contactForm.getEmail() + "\nPhone: " + contactForm.getPhone() + "\n\n" + contactForm.getMessage());

            mailSender.send(message);

            model.addAttribute("message", "Your message has been sent successfully.");
            return "redirect:/contact";
        } catch (Exception ex) {
            model.addAttribute("error", "Error: " + ex.getMessage());
            return "contact/contact";
        }
    }
}
