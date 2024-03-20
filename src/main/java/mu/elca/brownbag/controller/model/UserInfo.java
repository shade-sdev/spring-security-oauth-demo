package mu.elca.brownbag.controller.model;

import mu.elca.brownbag.security.model.OAuth2ProviderType;

import java.util.Set;
import java.util.UUID;

public record UserInfo(UUID id,
                       String username,
                       String displayName,
                       String email,
                       String avatar,
                       OAuth2ProviderType oAuth2ProviderType,
                       Set<String> authorities) {
}
