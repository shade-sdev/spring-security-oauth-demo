package mu.elca.brownbag.repository.role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "permission")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class PermissionJpaEntity {

    @Id
    @Column(name = "id", nullable = false)
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Column(name = "name")
    private String name;
}
