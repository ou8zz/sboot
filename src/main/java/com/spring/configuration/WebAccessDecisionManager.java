package com.spring.configuration;

import com.utils.RegexUtil;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by OLE on 2016/11/4.
 */
public class WebAccessDecisionManager implements AccessDecisionManager {

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
       /* if (authentication == null) {
            return;
        }
        return;*/

		//user role
        try {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            System.out.println("user role is = " + authorities);

            //url role
            Iterator<ConfigAttribute> ite = collection.iterator();

            int i = 0, j = 0;
            while (ite.hasNext()) {
                ConfigAttribute ca = ite.next();
                String needRole = ca.getAttribute();
                System.out.println("need role: "+needRole);
                System.out.println("URL:"+ o);
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
