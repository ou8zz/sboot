package com.spring.configuration;

import com.spring.model.Ztree;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @description 自定义资源的访问权限的定义加载器
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date 2016/11/14
 */
public class WebSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private SqlSession sqlSession;

    /* 应当是资源为key， 权限为value。 资源通常为url， 权限就是那些以ROLE_为前缀的角色。 一个资源可以由多个权限来访问。*/
    private static Map<String, Collection<ConfigAttribute>> resourceMap = null;

    /**
     * 返回请求资源URL所对应的权限
     * @param object 请求的资源URL
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        FilterInvocation filterInvocation = (FilterInvocation) object;
        HttpServletRequest request = filterInvocation.getHttpRequest();

        Iterator<String> ite = resourceMap.keySet().iterator();
        while (ite.hasNext()) {
            String resURL = ite.next();
            RequestMatcher requestMatcher = new AntPathRequestMatcher(resURL);
            if (requestMatcher.matches(request)) {
                requestMatcher = null;
                System.out.println("返回请求资源URL:" + request.getServletPath() + "所对应的权限:" + resourceMap.get(resURL));
                return resourceMap.get(resURL);
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    /**
     * @PostConstruct 是Java EE 5引入的注解，
     * Spring允许开发者在受管Bean中使用它。当DI容器实例化当前受管Bean时，
     * @PostConstruct 注解的方法会被自动触发，从而完成一些初始化工作， //加载所有资源与权限的关系
     */
    @PostConstruct
    private void loadResourceDefine() {
        resourceMap = new HashMap<String, Collection<ConfigAttribute>>();
        List<Ztree> lists = sqlSession.selectList("ztree.getRoleZtree");
        for (Ztree z : lists) {
            Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();

            // 以权限名封装为Spring的security Object
            ConfigAttribute configAttribute = new SecurityConfig(z.getName());
            boolean ru = resourceMap.containsKey(z.getUrl());
            if(ru) {
                configAttributes = resourceMap.get(z.getUrl());
                configAttributes.add(configAttribute);
            } else {
                configAttributes.add(configAttribute);
            }
            resourceMap.put(z.getUrl(), configAttributes);
        }
        System.out.println("加载完数据库中的所有资源和权限关系...");
    }
}
