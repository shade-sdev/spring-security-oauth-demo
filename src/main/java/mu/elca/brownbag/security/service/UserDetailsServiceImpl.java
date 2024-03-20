package mu.elca.brownbag.security.service;

import mu.elca.brownbag.security.model.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException
    {
        return UserPrincipal.builder()
                            .id(UUID.randomUUID())
                            .username(username)
                            .email("user@email.com")
                            .password(new BCryptPasswordEncoder().encode("shade"))
                            .build();
    }
}
