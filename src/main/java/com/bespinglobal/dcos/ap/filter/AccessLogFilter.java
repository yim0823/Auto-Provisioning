package com.bespinglobal.dcos.ap.filter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Project : Auto-Provisioning
 * Class : com.bespinglobal.dcos.ic.filter.AccessLogFilter
 * Version : 2019.07.22 v0.1
 * Created by taehyoung.yim on 2019-07-22.
 * *** 저작권 주의 ***
 */
@WebFilter(urlPatterns = {"/ic/*"}, description = "Access Log Filter", asyncSupported = true)
public class AccessLogFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(AccessLogFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("INIT LOG FILTER");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        String remoteAddr = StringUtils.defaultString(request.getRemoteAddr(), "-");
        String url = (req.getRequestURL() == null) ? "" : req.getRequestURL().toString();
        String queryString = StringUtils.defaultIfEmpty(req.getQueryString(), "");
        String refer = StringUtils.defaultString(req.getHeader("Refer"), "-");
        String agent = StringUtils.defaultString(req.getHeader("UserDto-Agent"), "-");
        String fullUrl = url;
        fullUrl += StringUtils.isNotEmpty(queryString) ? "?" + queryString : queryString;

        StringBuilder result = new StringBuilder();
        result
                .append(":")
                .append(remoteAddr)
                .append(":")
                .append(fullUrl)
                .append(":")
                .append(refer)
                .append(":")
                .append(agent);

        logger.info("LOG FILTER" + result.toString());

        long startDate = System.currentTimeMillis();
        chain.doFilter(req, response);
        long endDate = System.currentTimeMillis();
        String uri = req.getRequestURI();
        if (!uri.contains("swagger")) {
            logger.info(
                    "============\trestAPI Access Time:"
                            + (double) (endDate - startDate) / 1000
                            + "s\t============");
        }
    }

    @Override
    public void destroy() {
        logger.info("DESTROY LOG FILTER");
    }
}
