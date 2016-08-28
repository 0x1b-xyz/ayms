package io.stiefel.ayms.web

import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.web.servlet.config.annotation.*
import org.springframework.web.servlet.mvc.WebContentInterceptor

import javax.servlet.ServletContext

/**
 * Web configuration context.
 *
 * @author jason@stiefel.io
 */
@Configuration
@EnableWebMvc
@ComponentScan(['io.stiefel.ayms.web','asset.pipeline.springboot'])
@EnableTransactionManagement
class Context extends WebMvcConfigurerAdapter {

    @Autowired ServletContext servletContext

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.mediaType("json", MediaType.APPLICATION_JSON);
    }

    @Override
    void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable()
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserInterceptor())
                .addPathPatterns("/**").excludePathPatterns("/login/**");
        registry.addInterceptor(new WebContentInterceptor(
                cacheSeconds: 0,
                useExpiresHeader: true,
                useCacheControlHeader: true,
                useCacheControlNoStore: true
        ))
    }

    @Override
    void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp('/WEB-INF/views/', '.jsp')
    }

    @Override
    void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController('/').setViewName('redirect:/company')
    }

}
