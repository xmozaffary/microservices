package se.moln.productservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

class RequestIdFilterTest {

    @Test
    void generatesRequestIdWhenMissingAndEchoesHeader() throws ServletException, IOException {
        RequestIdFilter filter = new RequestIdFilter();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        AtomicReference<String> mdcSeen = new AtomicReference<>();
        FilterChain chain = (req, res) -> mdcSeen.set(MDC.get(RequestIdFilter.MDC_KEY));

        filter.doFilter(request, response, chain);

        String echoed = response.getHeader(RequestIdFilter.HEADER);
        assertThat(echoed).isNotBlank();
        assertThat(mdcSeen.get()).isEqualTo(echoed);
        // After filter completes, MDC should be cleared
        assertThat(MDC.get(RequestIdFilter.MDC_KEY)).isNull();
    }

    @Test
    void preservesIncomingRequestId() throws ServletException, IOException {
        RequestIdFilter filter = new RequestIdFilter();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(RequestIdFilter.HEADER, "req-123");
        MockHttpServletResponse response = new MockHttpServletResponse();

        AtomicReference<String> mdcSeen = new AtomicReference<>();
        FilterChain chain = (req, res) -> mdcSeen.set(MDC.get(RequestIdFilter.MDC_KEY));

        filter.doFilter(request, response, chain);

        assertThat(response.getHeader(RequestIdFilter.HEADER)).isEqualTo("req-123");
        assertThat(mdcSeen.get()).isEqualTo("req-123");
        assertThat(MDC.get(RequestIdFilter.MDC_KEY)).isNull();
    }
}