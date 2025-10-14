package se.moln.productservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.BodyFilter;
import org.zalando.logbook.HeaderFilter;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.HttpHeaders;
import java.util.regex.Pattern;

@Configuration
public class LogbookConfig {

    @Bean
    public Logbook logbook() {
        return Logbook.builder()
                .headerFilter(maskAuthorizationHeader())
                .bodyFilter(maskSensitiveJsonFields())
                .build();
    }

    private HeaderFilter maskAuthorizationHeader() {
        return headers -> headers.apply(
                HttpHeaders.predicate(name -> "Authorization".equalsIgnoreCase(name)),
                (n, v) -> java.util.Collections.singleton("*****")
        );
    }

    private BodyFilter maskSensitiveJsonFields() {
        // Simple regex masking for JSON string fields named password and token
        Pattern passwordPattern = Pattern.compile("(\\\"password\\\"\\s*:\\s*\\\")[^\\\"]*(\\\")", Pattern.CASE_INSENSITIVE);
        Pattern tokenPattern = Pattern.compile("(\\\"token\\\"\\s*:\\s*\\\")[^\\\"]*(\\\")", Pattern.CASE_INSENSITIVE);

        return (contentType, body) -> {
            if (body == null || body.isBlank()) {
                return body;
            }
            String masked = passwordPattern.matcher(body).replaceAll("$1*****$2");
            masked = tokenPattern.matcher(masked).replaceAll("$1*****$2");
            return masked;
        };
    }
}