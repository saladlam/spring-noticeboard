package info.saladlam.example.spring.noticeboard.framework;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.header.HeaderWriter;

public class CspHeaderWriter implements HeaderWriter {

    private static final String CONTENT_SECURITY_POLICY_HEADER = "Content-Security-Policy";

    @Override
    public void writeHeaders(HttpServletRequest request, HttpServletResponse response) {
        if (!response.containsHeader(CONTENT_SECURITY_POLICY_HEADER)) {
            Object value = request.getAttribute(CspNonceFilter.ATTRIBUTE_NAME);
            response.setHeader(
                    CONTENT_SECURITY_POLICY_HEADER,
                    String.format("default-src 'self'%s data: cdnjs.cloudflare.com fonts.googleapis.com fonts.gstatic.com;", value instanceof String ? String.format(" 'nonce-%s'", value) : "")
            );
        }
    }

}
