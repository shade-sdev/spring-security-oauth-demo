package mu.elca.brownbag.controller;

import mu.elca.brownbag.controller.model.UserInfo;
import mu.elca.brownbag.controller.model.UserUpdateDto;
import mu.elca.brownbag.mapper.ApiMapper;
import mu.elca.brownbag.security.model.CustomAuth2User;
import mu.elca.brownbag.security.model.CustomPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class Controller {

    private final ApiMapper mapper;

    @Autowired
    public Controller(ApiMapper mapper)
    {
        this.mapper = mapper;
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyAuthority('OAUTH2_USER', 'NON_OAUTH_USER')")
    public ResponseEntity<UserInfo> me()
    {
        Object userPrincipal = SecurityContextHolder.getContext()
                                                    .getAuthentication()
                                                    .getPrincipal();

        if (userPrincipal instanceof CustomPrincipal userInfo) {
            return ResponseEntity.ok(mapper.mapToUserInfo(userInfo));
        }

        return ResponseEntity.of(Optional.empty());
    }

    @GetMapping("/discord")
    @Secured("ROLE_DISCORD")
    public ResponseEntity<CustomAuth2User> discord()
    {
        CustomAuth2User customAuth2User = (CustomAuth2User) SecurityContextHolder.getContext()
                                                                                 .getAuthentication()
                                                                                 .getPrincipal();

        return ResponseEntity.ok(customAuth2User);
    }

    @PutMapping("/users")
    @PreAuthorize("hasAnyAuthority('NON_OAUTH_USER', 'OAUTH2_USER') && #userUpdateDto.username == authentication.principal.username")
    public ResponseEntity<UserUpdateDto> updateUserDetails(@RequestBody UserUpdateDto userUpdateDto) {
        return ResponseEntity.ok(userUpdateDto);
    }

}
