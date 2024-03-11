package com.art.app.web.filter;

import com.art.app.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@WebFilter(filterName = "mdcFilter", urlPatterns = "/*")
public class MdcFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        boolean mdcFlag = false;
        try {
            MDC.put(Constants.MDC_ID, UUID.randomUUID().toString());
            mdcFlag = true;
        } catch (Throwable e) {
            log.error("MDC put error", e);
        }
        try {
            chain.doFilter(request, response);
        } finally {
            if (mdcFlag) {
                try {
                    MDC.remove(Constants.MDC_ID);
                } catch (Throwable e) {
                    log.error("MDC remove error", e);
                }
            }
        }
    }
}
