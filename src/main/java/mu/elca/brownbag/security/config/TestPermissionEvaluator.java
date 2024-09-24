package mu.elca.brownbag.security.config;

import mu.elca.brownbag.controller.model.UserUpdateDto;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import shade.dev.local.security.type.permissionevaluator.TargetedPermissionEvaluator;

import java.io.Serializable;

@Component
public class TestPermissionEvaluator implements TargetedPermissionEvaluator {
    @Override
    public String getTargetType() {
        return UserUpdateDto.class.getName();
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return true;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return true;
    }
}
