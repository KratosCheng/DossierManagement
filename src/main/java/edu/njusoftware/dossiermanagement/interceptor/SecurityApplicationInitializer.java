package edu.njusoftware.dossiermanagement.interceptor;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.multipart.support.MultipartFilter;

import javax.servlet.ServletContext;

/**
 * 将multipartfilter置于springSecurityFilterChain之上，这样就上传文件的时候就会先被multipartfilter拦截，而不会受到security的保护
 */
public class SecurityApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

    @Override
    protected void beforeSpringSecurityFilterChain(ServletContext servletContext) {
        insertFilters(servletContext, new MultipartFilter());
    }
}
