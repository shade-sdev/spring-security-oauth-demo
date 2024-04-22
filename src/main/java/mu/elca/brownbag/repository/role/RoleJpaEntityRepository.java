package mu.elca.brownbag.repository.role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleJpaEntityRepository extends JpaRepository<RoleJpaEntity, UUID> {
}
