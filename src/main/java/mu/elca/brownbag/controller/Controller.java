package mu.elca.brownbag.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mu.elca.brownbag.security.model.CustomAuth2User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Optional;

@RestController
public class Controller {

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('OAUTH2_USER')")
    public ResponseEntity<CustomAuth2User> me()
    {
        CustomAuth2User customAuth2User = (CustomAuth2User) SecurityContextHolder.getContext()
                                                                                 .getAuthentication()
                                                                                 .getPrincipal();

        return ResponseEntity.ok(customAuth2User);
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

}
