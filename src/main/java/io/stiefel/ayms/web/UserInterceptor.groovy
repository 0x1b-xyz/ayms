package io.stiefel.ayms.web

import groovy.util.logging.Log4j
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Forces a user session to exist
 *
 * @author jason@stiefel.io
 */
@Log4j
class UserInterceptor extends HandlerInterceptorAdapter {

    @Override
    boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.info("Looking for user ...")
        super.preHandle(request, response, handler)
    }
}
