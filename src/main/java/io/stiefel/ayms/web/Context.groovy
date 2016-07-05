package io.stiefel.ayms.web

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.servlet.config.annotation.*

/**
 * Web configuration context.
 *
 * @author jason@stiefel.io
 */
@Configuration
@EnableWebMvc
@ComponentScan('io.stiefel.ayms.web')
class Context extends WebMvcConfigurerAdapter {

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
    }

    @Override
    void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp('/WEB-INF/views/', '.jsp')
    }

    @Override
    void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController('/').setViewName('index')
    }

}
