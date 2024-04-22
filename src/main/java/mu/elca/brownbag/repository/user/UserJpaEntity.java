package mu.elca.brownbag.repository.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mu.elca.brownbag.repository.role.RoleJpaEntity;
import mu.elca.brownbag.security.model.AuthProviderType;

import java.util.UUID;

@Entity
@Table(name = "user_principal")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserJpaEntity {

    @Id
    @Column(name = "id", nullable = false)
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "avatar", length = 1000)
    private String avatar;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_provider_type")
    private AuthProviderType authProviderType;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private RoleJpaEntity role;

}
