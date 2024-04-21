package mu.elca.brownbag.mapper;

import mu.elca.brownbag.controller.model.UserInfo;
import mu.elca.brownbag.security.model.CustomPrincipal;
import org.mapstruct.Mapper;
import org.springframework.security.core.GrantedAuthority;

@Mapper(componentModel = "spring")
public interface ApiMapper {


    UserInfo mapToUserInfo(CustomPrincipal userPrincipal);

    default String mapToAuthority(GrantedAuthority grantedAuthority)
    {
        return grantedAuthority.getAuthority();
    }

}
