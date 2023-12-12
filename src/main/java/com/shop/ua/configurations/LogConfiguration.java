package com.shop.ua.configurations;

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

public class LogConfiguration {
    public static void configure() {
        Logger logger = (Logger) LoggerFactory.getLogger("org.springframework.web");
        logger.setLevel(Level.DEBUG);
    }
}

