package net.gspatace.json.schema.podo.generator.webapp;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author Jetbrains AI.
 */
class WebAppConfigurationFilterTest {

    @InjectMocks
    private WebAppConfigurationFilter filter;

    @Mock
    private WebappProperties properties;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void doFilter() throws IOException, ServletException {
        String overriddenContent = "{ \"prop\": \"value\" }";
        when(properties.toJson()).thenReturn(overriddenContent);

        // Act
        filter.doFilter(request, response, (req, res) -> res.getWriter().write("original content"));

        // Assert
        assertEquals(MediaType.APPLICATION_JSON_UTF8_VALUE, response.getContentType());
        assertEquals(overriddenContent, response.getContentAsString());
    }
}
