package mu.elca.brownbag.repository.role;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mu.elca.brownbag.security.model.RoleType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;
import java.util.UUID;

import static lombok.Builder.Default;

@Entity
@Table(name = "role")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class RoleJpaEntity {

    @Id
    @Column(name = "id", nullable = false)
    @Default
    private UUID id = UUID.randomUUID();

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private RoleType name;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    @Fetch(FetchMode.SUBSELECT)
    private List<PermissionJpaEntity> permissions;

}
