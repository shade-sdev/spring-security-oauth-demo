package mu.elca.brownbag.util;

import mu.elca.brownbag.repository.role.PermissionJpaEntity;
import mu.elca.brownbag.repository.role.PermissionJpaEntityRepository;
import mu.elca.brownbag.repository.role.RoleJpaEntity;
import mu.elca.brownbag.repository.role.RoleJpaEntityRepository;
import mu.elca.brownbag.repository.user.UserJpaEntity;
import mu.elca.brownbag.repository.user.UserJpaEntityRepository;
import mu.elca.brownbag.security.model.AuthProviderType;
import mu.elca.brownbag.security.model.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserInitializer implements CommandLineRunner {

    private final UserJpaEntityRepository repository;
    private final RoleJpaEntityRepository roleRepository;
    private final PermissionJpaEntityRepository permissionRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserInitializer(UserJpaEntityRepository repository,
                           RoleJpaEntityRepository roleRepository,
                           PermissionJpaEntityRepository permissionRepository)
    {
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public void run(String... args) throws Exception
    {

        PermissionJpaEntity nonOauth2UserPermission = PermissionJpaEntity.builder()
                                                                         .name("NON_OAUTH_USER")
                                                                         .build();

        PermissionJpaEntity userUpdatePermission = PermissionJpaEntity.builder()
                                                                      .name("USER_UPDATE")
                                                                      .build();
        permissionRepository.saveAll(List.of(userUpdatePermission, nonOauth2UserPermission));

        RoleJpaEntity userRole = RoleJpaEntity.builder()
                                              .name(RoleType.ROLE_USER)
                                              .permissions(List.of(nonOauth2UserPermission))
                                              .build();

        RoleJpaEntity adminRole = RoleJpaEntity.builder()
                                               .name(RoleType.ROLE_ADMIN)
                                               .permissions(List.of(nonOauth2UserPermission, userUpdatePermission))
                                               .build();

        roleRepository.saveAll(List.of(userRole, adminRole));

        UserJpaEntity user = UserJpaEntity.builder()
                                          .email("user@email.com")
                                          .authProviderType(AuthProviderType.NON_OAUTH2_USER)
                                          .username("user")
                                          .password(passwordEncoder.encode("user"))
                                          .role(userRole)
                                          .build();

        UserJpaEntity admin = UserJpaEntity.builder()
                                           .email("admin@email.com")
                                           .authProviderType(AuthProviderType.NON_OAUTH2_USER)
                                           .username("admin")
                                           .password(passwordEncoder.encode("admin"))
                                           .role(userRole)
                                           .build();

        repository.saveAll(List.of(user, admin));

    }
}
