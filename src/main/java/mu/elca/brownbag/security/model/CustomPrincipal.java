package mu.elca.brownbag.security.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public interface CustomPrincipal extends UserDetails {

    String getUsername();

    String getDisplayName();

    String getEmail();

    String getAvatar();

    AuthProviderType getAuthProviderType();

    Collection<? extends GrantedAuthority> getAuthorities();

}
