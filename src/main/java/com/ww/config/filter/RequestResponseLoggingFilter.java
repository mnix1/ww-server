package com.ww.config.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.ww.helper.RandomHelper.randomInteger;

//@Component
//@Order(2)
//public class RequestResponseLoggingFilter  extends GenericFilterBean {
//    private static final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);
//
//    @Override
//    public void doFilter(
//            ServletRequest request,
//            ServletResponse response,
//            FilterChain chain) throws IOException, ServletException {
//
//        HttpServletRequest req = (HttpServletRequest) request;
//        HttpServletResponse res = (HttpServletResponse) response;
//        try {
//            Thread.sleep(randomInteger(1000, 3000));
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
////        logger.info("Logging Request  {} : {}", req.getMethod(), req.getRequestURI());
//        chain.doFilter(request, response);
////        logger.info("Logging Response :{}", res.getContentType());
//    }
//}