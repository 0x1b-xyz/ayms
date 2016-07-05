package io.stiefel.ayms.web

import com.opensymphony.sitemesh.webapp.SiteMeshFilter
import org.springframework.web.filter.CharacterEncodingFilter
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer

import javax.servlet.Filter

/**
 * @author jason@stiefel.io
 */
class Initializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    final Class<?>[] rootConfigClasses = [io.stiefel.ayms.Context]
    final Class<?>[] servletConfigClasses = [Context]
    final String[] servletMappings = ['/']

    @Override
    protected Filter[] getServletFilters() {
        return [new CharacterEncodingFilter(encoding: "UTF-8"), new SiteMeshFilter()]
    }

}
