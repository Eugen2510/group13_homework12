package org.goit;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.goit.handlers.cookiehandler.CookieHandler;
import org.goit.handlers.paramhandler.ParamsHandler;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.IOException;
import java.net.URL;
import java.util.*;

@WebServlet("/time")
public class TimeServlet extends HttpServlet {
    private TemplateEngine engine;
    @Override
    public void init() {
        engine = new TemplateEngine();
        URL resource = this.getClass().getResource("/templates");
        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix(resource.getPath());
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        engine.addTemplateResolver(resolver);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String output = prepareResponseString(req, resp);

        Map<String, Object> params = new HashMap<>();
        params.put("time", output);

        Context simpleContext = new Context(
                req.getLocale(),
                params
        );
//        resp.setContentType("application/json");

        try {
            engine.process("time", simpleContext, resp.getWriter());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String prepareResponseString(HttpServletRequest req, HttpServletResponse resp) {
        ParamsHandler paramsHandler = new ParamsHandler(req);
        CookieHandler cookieHandler = new CookieHandler(req, resp);
        String timezone;
        Optional<Cookie> lastTimezone = cookieHandler.getNeededCookie("lastTimezone");

        if (!paramsHandler.hasParameter() && lastTimezone.isPresent()){
                timezone = lastTimezone.get().getValue();
        } else {
            timezone = paramsHandler.parseParam();
            cookieHandler.setLastTimezoneCookie(timezone);
        }

        return paramsHandler.getTimeForOutput(timezone);

    }
}
