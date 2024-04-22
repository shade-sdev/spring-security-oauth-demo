package mu.elca.brownbag.security.model;

import mu.elca.brownbag.exception.RequestCountException;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {


    private final Map<String, Integer> requestCounts = new HashMap<>();

    private static final int MAX_REQUESTS = 10;

    private static final long RESET_INTERVAL = 30 * 1000L;

    private long lastResetTime = System.currentTimeMillis();

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastResetTime >= RESET_INTERVAL) {
            requestCounts.clear();
            lastResetTime = System.currentTimeMillis();
        }

        boolean isDiscord = authentication.get()
                .getAuthorities()
                .stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("DISCORD"));

        if (!isDiscord){
            return new AuthorizationDecision(true);
        }

        int currentRequestCount = getRequestCountForUser(authentication.get().getName());

        if (currentRequestCount < MAX_REQUESTS) {
            incrementRequestCountForUser(authentication.get().getName());
            return new AuthorizationDecision(true);
        }

        throw new RequestCountException("Too many request");

    }

    private int getRequestCountForUser(String username) {
        return requestCounts.getOrDefault(username, 0);
    }


    private void incrementRequestCountForUser(String username) {
        requestCounts.put(username, getRequestCountForUser(username) + 1);
    }

}
