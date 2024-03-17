package mu.elca.brownbag.security.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Value
public class CustomAuth2User extends DefaultOAuth2User {

    String username;

    String displayName;

    String email;

    String avatar;

    OAuth2ProviderType OAuth2ProviderType;

    @Builder
    public CustomAuth2User(Collection<? extends GrantedAuthority> authorities,
                           Map<String, Object> attributes,
                           String nameAttributeKey,
                           String username,
                           String displayName,
                           String email, String avatar,
                           OAuth2ProviderType OAuth2ProviderType)
    {
        super(authorities, attributes, nameAttributeKey);
        this.username = username;
        this.displayName = displayName;
        this.email = email;
        this.avatar = avatar;
        this.OAuth2ProviderType = OAuth2ProviderType;
    }

    public static CustomAuth2User fromOAuth2Provider(Collection<? extends GrantedAuthority> authorities,
                                                     Map<String, Object> attributes,
                                                     OAuth2ProviderType auth2ProviderType)
    {
        List<String> currentAuthorities = authorities.stream()
                                                     .map(GrantedAuthority::getAuthority)
                                                     .collect(Collectors.toList());

        currentAuthorities.add(auth2ProviderType.name());
        currentAuthorities.add("ROLE_" + auth2ProviderType.name());
        var updatedAuthorities = currentAuthorities.stream()
                                                   .map(SimpleGrantedAuthority::new)
                                                   .toList();

        return CustomAuth2User.builder()
                              .username((String) attributes.get(auth2ProviderType.getUserName()))
                              .displayName((String) attributes.get(auth2ProviderType.getDisplayName()))
                              .email((String) attributes.get(auth2ProviderType.getEmail()))
                              .avatar(getAvatarUrl(attributes, auth2ProviderType))
                              .authorities(updatedAuthorities)
                              .attributes(attributes)
                              .nameAttributeKey(auth2ProviderType.getUserNameAttributeName())
                              .OAuth2ProviderType(auth2ProviderType)
                              .build();
    }

    private static String getAvatarUrl(Map<String, Object> attributes, OAuth2ProviderType auth2ProviderType)
    {
        return switch (auth2ProviderType) {
            case GOOGLE -> (String) attributes.get(auth2ProviderType.getPicture());
            case DISCORD -> String.format("https://cdn.discordapp.com/avatars/%s/%s",
                                          attributes.get("id"),
                                          attributes.get(auth2ProviderType.getPicture()));
            case GITHUB -> String.format("https://ui-avatars.com/api/?name=%s",
                                         attributes.get(auth2ProviderType.getDisplayName()));
        };
    }

}