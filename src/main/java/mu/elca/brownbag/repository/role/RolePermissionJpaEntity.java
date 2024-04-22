package mu.elca.brownbag.repository.role;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "role_permission")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class RolePermissionJpaEntity {

    @EmbeddedId
    private RolePermissionId id;

    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    static class RolePermissionId {
        @Column(name = "role_id")
        private UUID roleId;

        @Column(name = "permission_id")
        private UUID permissionId;
    }

}
