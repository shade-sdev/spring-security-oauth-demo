package mu.elca.brownbag.controller.model;

import mu.elca.brownbag.security.model.AuthProviderType;

import java.util.Set;
import java.util.UUID;

public record UserInfo(UUID id,
                       String username,
                       String displayName,
                       String email,
                       String avatar,
                       AuthProviderType authProviderType,
                       Set<String> authorities) {
}
