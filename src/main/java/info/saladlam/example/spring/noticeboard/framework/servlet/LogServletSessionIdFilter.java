package info.saladlam.example.spring.noticeboard.framework.servlet;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.slf4j.MDC;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
public class LogServletSessionIdFilter extends HttpFilter implements OrderedFilter {

    public static final String MDC_KEY = "servletSessionId";

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            HttpSession session = request.getSession(false);
            if (Objects.nonNull(session)) {
                MDC.put(MDC_KEY, session.getId());
            } else {
                MDC.remove(MDC_KEY);
            }
            chain.doFilter(new InterceptHttpServletRequestWrapper(request), response);
        } finally {
            MDC.remove(MDC_KEY);
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    private static class InterceptHttpServletRequestWrapper extends HttpServletRequestWrapper {

        public InterceptHttpServletRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        private void keepSessionId(HttpSession session) {
            if (Objects.nonNull(session)) {
                MDC.put(MDC_KEY, session.getId());
            } else {
                MDC.remove(MDC_KEY);
            }
        }

        @Override
        public HttpSession getSession(boolean create) {
            HttpSession session = super.getSession(create);
            if (create) {
                keepSessionId(session);
            }
            return session;
        }

        @Override
        public HttpSession getSession() {
            HttpSession session = super.getSession();
            keepSessionId(session);
            return session;
        }

        @Override
        public String changeSessionId() {
            String sessionId = super.changeSessionId();
            MDC.put(MDC_KEY, sessionId);
            return sessionId;
        }

    }

}
