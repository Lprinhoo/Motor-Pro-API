package org.example.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    private static final Logger log = LoggerFactory.getLogger(User.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username; // Pode ser o email para usuários do Google

    @Column(nullable = true) // Senha pode ser nula para usuários de login social
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = true) // Nome do usuário do Google
    private String name;

    @Column(nullable = true, length = 500) // URL da foto de perfil do Google
    private String pictureUrl;

    @Column(nullable = true, unique = true) // ID único do Google
    private String googleId;

    @Column(nullable = false)
    private boolean isSocialLogin = false; // Indica se o usuário veio de login social

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oficina_id", unique = true)
    private Oficina oficina;

    @Column(nullable = false)
    private boolean owner = false;

    public User() {}

    // Construtor para registro manual
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.isSocialLogin = false;
    }

    // Construtor para login social (Google)
    public User(String email, String name, String pictureUrl, String googleId) {
        this.username = email; // Usar o email como username para login social
        this.email = email;
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.googleId = googleId;
        this.isSocialLogin = true;
        this.password = null; // Usuários sociais não têm senha local
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    @Override
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    @Override
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPictureUrl() { return pictureUrl; }
    public void setPictureUrl(String pictureUrl) { this.pictureUrl = pictureUrl; }

    public String getGoogleId() { return googleId; }
    public void setGoogleId(String googleId) { this.googleId = googleId; }

    public boolean isSocialLogin() { return isSocialLogin; }
    public void setSocialLogin(boolean socialLogin) { isSocialLogin = socialLogin; }

    public Oficina getOficina() { return oficina; }
    public void setOficina(Oficina oficina) { this.oficina = oficina; }

    public boolean isOwner() { return owner; }
    public void setOwner(boolean owner) { this.owner = owner; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = owner ? "ROLE_OWNER" : "ROLE_USER";
        log.debug("User {} has role: {}", username, role);
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked()      { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()               { return true; }
}
