package com.backend.foodproject.entity;

import com.backend.foodproject.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Phone number cannot be empty")
    @Column(unique = true)
    private String phoneNumber;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be empty")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean enabled = false;

    @Column(unique = true)
    private  String verificationToken;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Cart cart;

}
