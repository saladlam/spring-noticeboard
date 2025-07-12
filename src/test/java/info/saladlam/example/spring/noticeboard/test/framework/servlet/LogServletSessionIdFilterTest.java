package info.saladlam.example.spring.noticeboard.test.framework.servlet;

import info.saladlam.example.spring.noticeboard.framework.servlet.LogServletSessionIdFilter;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.slf4j.MDC;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class LogServletSessionIdFilterTest {

    @TestConfiguration
    static class LogServletSessionIdFilterTestContextConfiguration {
    }

    private static final LogServletSessionIdFilter FILTER_TEST = new LogServletSessionIdFilter();
    private static final String MDC_KEY = LogServletSessionIdFilter.MDC_KEY;

    @MockitoBean
    private HttpServletRequest request;
    @MockitoBean
    private HttpSession session;
    @MockitoBean
    private HttpServletResponse response;

    private HttpServlet servlet;
    private OncePerRequestFilter filter;

    @BeforeEach
    void init() {
        servlet = new HttpServlet() {
        };
        filter = new OncePerRequestFilter() {

            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                filterChain.doFilter(request, response);
            }

        };
        MDC.remove(MDC_KEY);
    }

    @AfterEach
    void teardown() {
        MDC.remove(MDC_KEY);
    }

    @Test
    void mdcIsClearAfterFilter() throws ServletException, IOException {
        String id = "A2A7643E459053A629ECE5FCA129C1F8";
        OncePerRequestFilter upperFilter = new OncePerRequestFilter() {

            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                MDC.put(MDC_KEY, id);
            }

        };

        MockFilterChain chain = new MockFilterChain(servlet, FILTER_TEST, upperFilter);
        filter.doFilter(request, response, chain);

        assertThat(MDC.get(MDC_KEY), nullValue());
    }

    @Test
    void testNullSession() throws ServletException, IOException {
        OncePerRequestFilter upperFilter = new OncePerRequestFilter() {

            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                assertThat(MDC.get(MDC_KEY), nullValue());
            }

        };
        Mockito.when(request.getSession(false)).thenReturn(null);

        MockFilterChain chain = new MockFilterChain(servlet, FILTER_TEST, upperFilter);
        filter.doFilter(request, response, chain);

        verify(request, times(1)).getSession(false);
    }

    @Test
    void testGetNullSession() throws ServletException, IOException {
        OncePerRequestFilter upperFilter = new OncePerRequestFilter() {

            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                assertThat(MDC.get(MDC_KEY), nullValue());
                request.getSession(false);
                assertThat(MDC.get(MDC_KEY), nullValue());
            }

        };
        Mockito.when(request.getSession(false)).thenReturn(null);

        MockFilterChain chain = new MockFilterChain(servlet, FILTER_TEST, upperFilter);
        filter.doFilter(request, response, chain);

        verify(request, times(2)).getSession(false);
    }

    @Test
    void testCreateNewSession() throws ServletException, IOException {
        String id = "3E4E8EB83D53D38F343E629037CFE35C";
        OncePerRequestFilter upperFilter = new OncePerRequestFilter() {

            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                assertThat(MDC.get(MDC_KEY), nullValue());
                request.getSession();
                assertThat(MDC.get(MDC_KEY), is(id));
            }

        };
        Mockito.when(request.getSession(false)).thenReturn(null);
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(session.getId()).thenReturn(id);

        MockFilterChain chain = new MockFilterChain(servlet, FILTER_TEST, upperFilter);
        filter.doFilter(request, response, chain);

        verify(request, times(1)).getSession(false);
        verify(request, times(1)).getSession();
        verify(session, times(1)).getId();
    }

    @Test
    void testGetSessionTrueParameter() throws ServletException, IOException {
        String id = "6FE2244129FC3E0D708B5176793BB3AE";
        OncePerRequestFilter upperFilter = new OncePerRequestFilter() {

            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                assertThat(MDC.get(MDC_KEY), nullValue());
                request.getSession(true);
                assertThat(MDC.get(MDC_KEY), is(id));
            }

        };
        Mockito.when(request.getSession(false)).thenReturn(null);
        Mockito.when(request.getSession(true)).thenReturn(session);
        Mockito.when(session.getId()).thenReturn(id);

        MockFilterChain chain = new MockFilterChain(servlet, FILTER_TEST, upperFilter);
        filter.doFilter(request, response, chain);

        verify(request, times(1)).getSession(false);
        verify(request, times(1)).getSession(true);
        verify(session, times(1)).getId();
    }

    @Test
    void testSessionExists() throws ServletException, IOException {
        String id = "071D0227C5221354042AB4787430F294";
        OncePerRequestFilter upperFilter = new OncePerRequestFilter() {

            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                assertThat(MDC.get(MDC_KEY), is(id));
            }

        };
        Mockito.when(request.getSession(false)).thenReturn(session);
        Mockito.when(session.getId()).thenReturn(id);

        MockFilterChain chain = new MockFilterChain(servlet, FILTER_TEST, upperFilter);
        filter.doFilter(request, response, chain);

        verify(request, times(1)).getSession(false);
        verify(session, times(1)).getId();
    }

    @Test
    void testGetCurrentSession() throws ServletException, IOException {
        String id = "1443FB40CBB86008373CDC02759DD16A";
        OncePerRequestFilter upperFilter = new OncePerRequestFilter() {

            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                assertThat(MDC.get(MDC_KEY), is(id));
                request.getSession(true);
                assertThat(MDC.get(MDC_KEY), is(id));
            }

        };
        Mockito.when(request.getSession(false)).thenReturn(session);
        Mockito.when(request.getSession(true)).thenReturn(session);
        Mockito.when(session.getId()).thenReturn(id);

        MockFilterChain chain = new MockFilterChain(servlet, FILTER_TEST, upperFilter);
        filter.doFilter(request, response, chain);

        verify(request, times(1)).getSession(false);
        verify(request, times(1)).getSession(true);
        verify(session, times(2)).getId();
    }

    @Test
    void testChangeSessionId() throws ServletException, IOException {
        String oldId = "B3BE8363D66517F8E3C4AD17B21102A9";
        String newId = "6E1A8F487A5734EE5966A3A12CB3D2E8";
        OncePerRequestFilter upperFilter = new OncePerRequestFilter() {

            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                assertThat(MDC.get(MDC_KEY), is(oldId));
                request.changeSessionId();
                assertThat(MDC.get(MDC_KEY), is(newId));
            }

        };
        Mockito.when(request.getSession(false)).thenReturn(session);
        Mockito.when(session.getId()).thenReturn(oldId);
        Mockito.when(request.changeSessionId()).thenReturn(newId);

        MockFilterChain chain = new MockFilterChain(servlet, FILTER_TEST, upperFilter);
        filter.doFilter(request, response, chain);

        verify(request, times(1)).getSession(false);
        verify(session, times(1)).getId();
        verify(request, times(1)).changeSessionId();
    }

}
