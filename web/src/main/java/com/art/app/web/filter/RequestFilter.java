package com.art.app.web.filter;

import com.art.app.web.bean.MyRequestWrapper;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author longhuang
 */
@Component
@WebFilter(filterName = "requestFilter", urlPatterns = "/*")
public class RequestFilter implements Filter {

    private List<String> ignoreList = Arrays.asList("/payment/feedback/wechat/notify",
            "/payment/feedback/wechat/refund/notify",
            "/payment/feedback/alipay/notify",
            "/payment/feedback/alipay/refund/notify");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ServletRequest requestWrapper = null;
        if (servletRequest instanceof HttpServletRequest) {
            String requestURI = ((HttpServletRequest) servletRequest).getRequestURI();
            if (!ignoreList.contains(requestURI)) {
                requestWrapper = new MyRequestWrapper((HttpServletRequest) servletRequest);
            }
        }
        if (null == requestWrapper) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            filterChain.doFilter(requestWrapper, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}

