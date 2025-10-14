package se.moln.productservice.health;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import se.moln.productservice.repository.ProductRepository;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
public class AppHealthIndicator implements HealthIndicator {

    private final ProductRepository productRepository;
    private final Environment env;
    private final String uploadDir;
    private final Instant startedAt = Instant.now();

    public AppHealthIndicator(ProductRepository productRepository,
                              Environment env,
                              @Value("${app.upload.dir:uploads}") String uploadDir) {
        this.productRepository = productRepository;
        this.env = env;
        this.uploadDir = uploadDir;
    }

    @Override
    public Health health() {
        Map<String, Object> details = new HashMap<>();

        // Uptime
        Duration uptime = Duration.between(startedAt, Instant.now());
        details.put("uptime", uptime.toString());

        // App info
        details.put("app", Map.of(
                "name", env.getProperty("spring.application.name", "product-service"),
                "profile", String.join(",", env.getActiveProfiles())
        ));

        // Database check
        try {
            long count = productRepository.count();
            details.put("database", Map.of(
                    "status", "UP",
                    "products", count
            ));
        } catch (Exception ex) {
            return Health.down(ex).withDetail("database", Map.of("status", "DOWN", "error", ex.getMessage())).withDetails(details).build();
        }

        // Upload directory check (exists, writable, free space)
        try {
            Path path = Path.of(uploadDir);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            File f = path.toFile();
            boolean canWrite = f.canWrite();
            long freeBytes = f.getUsableSpace();
            details.put("storage", Map.of(
                    "path", f.getAbsolutePath(),
                    "writable", canWrite,
                    "freeBytes", freeBytes
            ));
        } catch (Exception ex) {
            return Health.down(ex).withDetail("storage", Map.of("status", "DOWN", "error", ex.getMessage())).withDetails(details).build();
        }

        return Health.up().withDetails(details).build();
    }
}