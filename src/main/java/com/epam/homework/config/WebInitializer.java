package com.epam.homework.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class WebInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.scan("com.epam.homework");
//        context.refresh();

        servletContext.addListener(new ContextLoaderListener(context));

        ServletRegistration.Dynamic appServlet = servletContext
                .addServlet("mvc", new DispatcherServlet(context));
        appServlet.setLoadOnStartup(1);
        appServlet.addMapping("/");
        System.out.println("Application is ready!");
    }
}