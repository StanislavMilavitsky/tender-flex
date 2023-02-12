package pl.exadel.milavitsky.tenderflex.entity.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    BIDDER,
    CONTRACTOR,
    ADMIN;


    @Override
    public String getAuthority() {
        return name();
    }
}
