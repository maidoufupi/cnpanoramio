package com.cnpanoramio.webapp.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.cnpanoramio.webapp.listener.StartupListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


/**
 * This class is used to reload the drop-downs initialized in the
 * StartupListener.
 *
 * <p>
 * <a href="ReloadController.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Controller
@RequestMapping("/map")
public class ExploreWorldController {
    private transient final Log log = LogFactory.getLog(ExploreWorldController.class);

    @RequestMapping(method = RequestMethod.GET)
    @SuppressWarnings("unchecked")
    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Entering 'execute' method");
        }

        return new ModelAndView("map");
    }
    
}
