package mu.elca.brownbag.repository.role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PermissionJpaEntityRepository extends JpaRepository<PermissionJpaEntity, UUID> {
}
