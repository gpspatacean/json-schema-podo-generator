package com.spatacean.json.schema.podo.generator.webapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet Stream where the initial HTTP Response content
 * will be written to. This will be wrapped in a {@link ByteArrayPrinter}.
 *
 * @author George Spătăcean
 */

class ByteArrayServletStream extends ServletOutputStream {

    final ByteArrayOutputStream byteArrayOutputStream;

    ByteArrayServletStream(final ByteArrayOutputStream byteArrayOutputStream) {
        this.byteArrayOutputStream = byteArrayOutputStream;
    }

    @Override
    public boolean isReady() {
        throw new UnsupportedOperationException("Not implemented!");
    }

    @Override
    public void setWriteListener(final WriteListener listener) {
        throw new UnsupportedOperationException("Not implemented!");
    }

    @Override
    public void write(final int b) {
        byteArrayOutputStream.write(b);
    }
}

/**
 * Wrapper over {@link ByteArrayServletStream}. This class will be used in
 * the {@link WebAppConfigurationFilter}, which does content overriding
 *
 * @author George Spătăcean
 */
class ByteArrayPrinter {

    private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private final PrintWriter printWriter = new PrintWriter(byteArrayOutputStream);
    private final ServletOutputStream servletOutputStream = new ByteArrayServletStream(byteArrayOutputStream);

    public PrintWriter getWriter() {
        return printWriter;
    }

    public ServletOutputStream getStream() {
        return servletOutputStream;
    }

    byte[] toByteArray() {
        printWriter.flush();
        return byteArrayOutputStream.toByteArray();
    }
}

/**
 * Simple Spring Filter that replaces contents of the front end webapp
 * provided configuration. This is done so that we can provide Spring-style
 * configurations to the Angular webapp
 *
 * @author George Spătăcean
 */
@Component
@Slf4j
public class WebAppConfigurationFilter implements Filter {

    private final WebappProperties properties;

    public WebAppConfigurationFilter(final WebappProperties properties) {
        this.properties = properties;
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        final HttpServletResponse servletResponse = (HttpServletResponse) response;
        final ByteArrayPrinter bap = new ByteArrayPrinter();

        final HttpServletResponse wrappedResponse = new HttpServletResponseWrapper(servletResponse) {
            @Override
            public ServletOutputStream getOutputStream() {
                return bap.getStream();
            }

            @Override
            public PrintWriter getWriter() {
                return bap.getWriter();
            }
        };

        chain.doFilter(request, wrappedResponse);

        final String originalContent = new String(bap.toByteArray());
        log.trace("Original configuration provided\n: {}", originalContent);
        final String overriddenContent = properties.toJson();
        log.trace("Overridden content\n: {}", overriddenContent);
        response.reset();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(overriddenContent);
        response.setContentLength(overriddenContent.length());
    }
}
