package mu.elca.brownbag.security.service;

import mu.elca.brownbag.security.model.AuthProviderType;
import mu.elca.brownbag.security.model.CustomAuth2User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public CustomAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException
    {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        AuthProviderType auth2ProviderType = AuthProviderType.getProviderType(userRequest.getClientRegistration().getRegistrationId());

        return CustomAuth2User.fromOAuth2Provider(oAuth2User.getAuthorities(),
                                                  oAuth2User.getAttributes(),
                                                  auth2ProviderType);
    }
}
