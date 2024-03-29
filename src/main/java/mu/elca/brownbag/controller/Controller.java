package mu.elca.brownbag.controller;

import mu.elca.brownbag.controller.model.UserInfo;
import mu.elca.brownbag.controller.model.UserUpdateDto;
import mu.elca.brownbag.mapper.ApiMapper;
import mu.elca.brownbag.security.model.CustomAuth2User;
import mu.elca.brownbag.security.model.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

        if (userPrincipal instanceof CustomAuth2User userInfo) {
            return ResponseEntity.ok(mapper.mapToUserInfo(userInfo));
        }

        if (userPrincipal instanceof UserPrincipal userInfo) {
            return ResponseEntity.ok(mapper.mapToUserInfo(userInfo));
        }

        return ResponseEntity.of(Optional.empty());
    }

    @GetMapping("/discord")
    @PreAuthorize("hasAuthority('ROLE_DISCORD')")
    public ResponseEntity<CustomAuth2User> discord()
    {
        CustomAuth2User customAuth2User = (CustomAuth2User) SecurityContextHolder.getContext()
                                                                                 .getAuthentication()
                                                                                 .getPrincipal();

        return ResponseEntity.ok(customAuth2User);
    }

    @PostMapping("/users")
    public ResponseEntity<UserUpdateDto> updateUserDetails(@RequestBody UserUpdateDto userUpdateDto)
    {
        return ResponseEntity.ok(userUpdateDto);
    }

}
