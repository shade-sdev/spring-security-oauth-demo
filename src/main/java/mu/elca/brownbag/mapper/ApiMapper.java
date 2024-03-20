package mu.elca.brownbag.mapper;

import mu.elca.brownbag.controller.model.UserInfo;
import mu.elca.brownbag.security.model.CustomAuth2User;
import mu.elca.brownbag.security.model.UserPrincipal;
import org.mapstruct.Mapper;
import org.springframework.security.core.GrantedAuthority;

@Mapper(componentModel = "spring")
public interface ApiMapper {

    UserInfo mapToUserInfo(CustomAuth2User customAuth2User);

    UserInfo mapToUserInfo(UserPrincipal userPrincipal);

    default String mapToAuthority(GrantedAuthority grantedAuthority)
    {
        return grantedAuthority.getAuthority();
    }

}
