package se.moln.productservice.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import static org.assertj.core.api.Assertions.*;

class CorsConfigTest {

    @Test
    void addCorsMappings_ShouldConfigureExpectedOriginsAndMethods() {
        CorsConfig cfg = new CorsConfig();
        TestCorsRegistry reg = new TestCorsRegistry();

        cfg.addCorsMappings(reg);

        assertThat(reg.pattern).isEqualTo("/**");
        assertThat(reg.allowedOrigins)
                .contains("http://localhost:3000", "https://productservice.drillbi.se/", "https://userservice.drillbi.se");
        assertThat(reg.allowedMethods)
                .contains("GET", "POST", "PUT", "DELETE", "OPTIONS");
        assertThat(reg.allowedHeaders).contains("*");
    }

    // Minimal stub capturing what our CorsConfig sets on the registry
    static class TestCorsRegistry extends CorsRegistry {
        String pattern;
        java.util.List<String> allowedOrigins = new java.util.ArrayList<>();
        java.util.List<String> allowedMethods = new java.util.ArrayList<>();
        java.util.List<String> allowedHeaders = new java.util.ArrayList<>();

        @Override
        public org.springframework.web.servlet.config.annotation.CorsRegistration addMapping(String pathPattern) {
            this.pattern = pathPattern;
            return new org.springframework.web.servlet.config.annotation.CorsRegistration(pathPattern) {
                @Override
                public org.springframework.web.servlet.config.annotation.CorsRegistration allowedOrigins(String... origins) {
                    java.util.Collections.addAll(allowedOrigins, origins);
                    return this;
                }
                @Override
                public org.springframework.web.servlet.config.annotation.CorsRegistration allowedMethods(String... methods) {
                    java.util.Collections.addAll(allowedMethods, methods);
                    return this;
                }
                @Override
                public org.springframework.web.servlet.config.annotation.CorsRegistration allowedHeaders(String... headers) {
                    java.util.Collections.addAll(allowedHeaders, headers);
                    return this;
                }
            };
        }
    }
}
