package com.spring.configuration;

import com.utils.RegexUtil;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Iterator;

/**
 * @description 验证用户对应url权限
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date 2016/11/14
 */
public class WebAccessDecisionManager implements AccessDecisionManager {

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection)
            throws AccessDeniedException, InsufficientAuthenticationException {
		//user role
        try {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            Iterator<ConfigAttribute> ite = collection.iterator();  //url role

            int i = 0, j = 0;
            while (ite.hasNext()) {
                ConfigAttribute ca = ite.next();
                String needRole = "ROLE_"+ca.getAttribute();
                // ga 为用户所被赋予的权限。 needRole 为访问相应的资源应该具有的权限。
                for (GrantedAuthority ga : authorities) {
                    if (RegexUtil.notEmpty(needRole) && needRole.trim().equals(ga.getAuthority().trim())) {
                        i++;
                        return;
                    }
                }
                j++;
            }
            if(i == j) return;
        } catch (Exception e) {
            return;
        }
        throw new AccessDeniedException("Access refused to !");
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
