package mu.elca.brownbag.security.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum OAuth2ProviderType {

    DISCORD("discord", "username", "global_name", "email", "avatar", "username"),
    GITHUB("github", "login", "name", "email", "", "id"),
    GOOGLE("google", "sub", "name", "email", "picture", "sub"),
    NON_OAUTH2_USER(null, null, null, null, null, null);

    private final String provider;

    private final String userName;

    private final String displayName;

    private final String email;

    private final String picture;

    private final String userNameAttributeName;

    public static OAuth2ProviderType getProviderType(String provider)
    {
        return Arrays.stream(OAuth2ProviderType.values())
                     .filter(it -> it.getProvider().equalsIgnoreCase(provider))
                     .findAny()
                     .orElseThrow(() -> new IllegalArgumentException("No such provider: " + provider));
    }
}
