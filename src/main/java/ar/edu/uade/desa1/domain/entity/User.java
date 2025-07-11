package ar.edu.uade.desa1.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "users")
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "dni", nullable = false, unique = true, length = 8)
    private String dni;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "active")
    private Boolean active;

    @Column
    private String resetToken;

    @Column
    private LocalDateTime resetTokenExpiry;

    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    @Column(name = "verification_code_expiry")
    private LocalDateTime verificationCodeExpiry;

    @Column(name = "email_verified")
    private Boolean emailVerified;


    @Column(name = "push_token")
    private String pushToken;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.getName().toUpperCase()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }


    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }


}
