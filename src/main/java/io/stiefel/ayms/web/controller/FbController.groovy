package io.stiefel.ayms.web.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView

/**
 * @author jason@stiefel.io
 */
@Controller
@RequestMapping(value = '/form')
class FbController {

    @RequestMapping(method = RequestMethod.GET, produces = 'text/html')
    ModelAndView index() {
        new ModelAndView('form')
    }


}
