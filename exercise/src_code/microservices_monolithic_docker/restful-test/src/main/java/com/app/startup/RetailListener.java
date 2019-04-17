package com.app.startup;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by Shekhar.Agrawal on 1/17/2018.
 */
public class RetailListener implements ServletContextListener {
    public RetailListener() {}

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            System.out.println("Context Init");
            System.out.println("Hello World !!");
//            DataLoader.run();
        } catch (Exception e) {
            System.err.println("Error in Start up Listener");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Context Destroyed");
    }
}