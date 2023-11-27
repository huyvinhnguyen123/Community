package Blind.Sight.community.domain.entity;

import Blind.Sight.community.util.common.DeleteFlag;
import Blind.Sight.community.util.common.LockFlag;
import com.google.common.collect.Lists;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "Users")
public class User implements UserDetails {
    @Id
    @Column(name = "userId", updatable = false)
    private String userId;
    @Column(name = "userName", unique = true, nullable = false)
    private String userName;
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "birthDate", nullable = false)
    private LocalDate birthDate;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "role", nullable = false)
    private String role;

    // For prepare deleting user
    @Column(name = "lockFlag", nullable = false)
    private int lockFlag;
    @Column(name = "deleteFlag", nullable = false)
    private int deleteFlag;
    @Column(name = "oldLoginId")
    private String oldLoginId;

    @Column(name = "createAt")
    private LocalDate createAt;
    @Column(name = "updateAt")
    private LocalDate updateAt;


    public User() {
        this.userId = UUID.randomUUID().toString();
        this.createAt = LocalDate.now();
        this.updateAt = LocalDate.now();
    }

    public User(String userName, String email, String role) {
        this.userId = UUID.randomUUID().toString();
        this.userName = userName;
        this.email = email;
        this.role = role;
        this.createAt = LocalDate.now();
        this.updateAt = LocalDate.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (StringUtils.hasText(role)) {
            return Lists.newArrayList(new SimpleGrantedAuthority(role));
        } else {
            return Lists.newArrayList();
        }
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return lockFlag == LockFlag.NON_LOCK.getCode();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return deleteFlag == DeleteFlag.NON_DELETE.getCode();
    }
}
