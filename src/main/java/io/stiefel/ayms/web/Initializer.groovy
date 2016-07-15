package io.stiefel.ayms.web

import asset.pipeline.AssetPipelineConfigHolder
import asset.pipeline.fs.FileSystemAssetResolver
import asset.pipeline.servlet.AssetPipelineDevFilter
import com.opensymphony.sitemesh.webapp.SiteMeshFilter
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.context.support.WebApplicationContextUtils
import org.springframework.web.filter.CharacterEncodingFilter
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer

import javax.servlet.DispatcherType
import javax.servlet.Filter
import javax.servlet.FilterRegistration
import javax.servlet.ServletContext
import javax.servlet.ServletException

/**
 * @author jason@stiefel.io
 */
class Initializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    final Class<?>[] rootConfigClasses = [io.stiefel.ayms.Context]
    final Class<?>[] servletConfigClasses = [Context]
    final String[] servletMappings = ['/']

    @Override
    void onStartup(ServletContext servletContext) throws ServletException {

//        WebApplicationContext ctx = WebApplicationContextUtils.findWebApplicationContext(servletContext)
//
//        10.times { println "!!!! ${ctx.environment.getProperty('ayms.jdbc.url')}" }

        AssetPipelineConfigHolder.registerResolver(
                new FileSystemAssetResolver('testAssets', 'src/main/webapp/assets')
        )
        AssetPipelineConfigHolder.config = [
                handlebars: [
                        'templateRoot': 'hbs',
                        'templatePathSeperator': '/'
                ]
        ]
        FilterRegistration.Dynamic registration = servletContext.addFilter('assets-filter',
                new AssetPipelineDevFilter(mapping: 'assets'))
        registration.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/assets/*")

        super.onStartup(servletContext)
    }

    @Override
    protected Filter[] getServletFilters() {
        [new CharacterEncodingFilter(encoding: "UTF-8"), new SiteMeshFilter()]
    }

}
