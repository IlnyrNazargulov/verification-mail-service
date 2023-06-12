package ru.ilnur.verificationmailservice.web.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import ru.ilnur.verificationmailservice.facade.model.enums.Role;

@Getter
@RequiredArgsConstructor
public class RoleAuthority implements GrantedAuthority {
    private final Role role;

    @Override
    public String getAuthority() {
        return role.name();
    }
}
