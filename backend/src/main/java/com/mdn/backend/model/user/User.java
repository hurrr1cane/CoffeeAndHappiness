package com.mdn.backend.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mdn.backend.model.order.Order;
import com.mdn.backend.model.review.CafeReview;
import com.mdn.backend.model.review.FoodReview;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name")
    @NotNull(message = "First name is required")
    private String firstName;

    @Column(name = "last_name")
    @NotNull(message = "Last name is required")
    private String lastName;

    @Column(name = "email")
    @NotNull(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @Column(name = "password")
    @NotNull(message = "Password is required")
    private String password;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "image_url")
    @Pattern(regexp = "^https?://.*",
            message = "Image URL must start with http:// or https://")
    private String imageUrl;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    @Hidden
    private Role role;

    @Column(name = "bonus_points")
    @Min(value = 0, message = "Bonus points must be greater than 0")
    private int bonusPoints;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FoodReview> foodReviews;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CafeReview> cafeReviews;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return enabled;
    }
}
