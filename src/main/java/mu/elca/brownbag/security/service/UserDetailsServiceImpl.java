package mu.elca.brownbag.security.service;

import mu.elca.brownbag.repository.role.PermissionJpaEntity;
import mu.elca.brownbag.repository.role.RoleJpaEntity;
import mu.elca.brownbag.repository.user.UserJpaEntity;
import mu.elca.brownbag.repository.user.UserJpaEntityRepository;
import mu.elca.brownbag.security.model.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserJpaEntityRepository userJpaEntityRepository;

    @Autowired
    public UserDetailsServiceImpl(UserJpaEntityRepository userJpaEntityRepository)
    {
        this.userJpaEntityRepository = userJpaEntityRepository;
    }

    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException
    {
        UserJpaEntity user = userJpaEntityRepository.findByUsername(username)
                                                    .orElseThrow(() -> new UsernameNotFoundException(username));
        return UserPrincipal.builder()
                            .id(user.getId())
                            .username(user.getUsername())
                            .email(user.getEmail())
                            .password(user.getPassword())
                            .authProviderType(user.getAuthProviderType())
                            .authorities(getAuthorities(user))
                            .build();
    }

    public Collection<? extends GrantedAuthority> getAuthorities(UserJpaEntity userJpaEntity)
    {
        return Stream.concat(Optional.ofNullable(userJpaEntity.getRole())
                                     .map(RoleJpaEntity::getName)
                                     .map(String::valueOf)
                                     .stream(),

                             Optional.ofNullable(userJpaEntity.getRole())
                                     .map(RoleJpaEntity::getPermissions)
                                     .stream()
                                     .flatMap(Collection::stream)
                                     .map(PermissionJpaEntity::getName))

                     .map(SimpleGrantedAuthority::new)
                     .collect(Collectors.toSet());
    }


}
