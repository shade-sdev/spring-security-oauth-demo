package mu.elca.brownbag.security.model;

import lombok.Builder;
import lombok.Value;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import static lombok.Builder.Default;

@Value
@Builder
public class UserPrincipal implements CustomPrincipal {

    UUID id;

    String username;

    String password;

    String email;

    @Default
    AuthProviderType authProviderType = AuthProviderType.NON_OAUTH2_USER;

    @Default
    Collection<? extends GrantedAuthority> authorities = Set.of();

    @Override
    public String getDisplayName()
    {
        return this.username;
    }

    @Override
    public String getAvatar()
    {
        return String.format("https://ui-avatars.com/api/?name=%s", this.getUsername());
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }
}
