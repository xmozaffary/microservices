package se.moln.productservice.health;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;
import se.moln.productservice.repository.ProductRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class AppHealthIndicatorTest {

    @Test
    void health_up_when_db_and_storage_ok() throws IOException {
        ProductRepository repo = Mockito.mock(ProductRepository.class);
        Mockito.when(repo.count()).thenReturn(3L);
        Environment env = Mockito.mock(Environment.class);
        Mockito.when(env.getProperty("spring.application.name", "product-service")).thenReturn("product-service");
        Mockito.when(env.getActiveProfiles()).thenReturn(new String[]{});

        Path tmp = Files.createTempDirectory("uploads-test");
        AppHealthIndicator ind = new AppHealthIndicator(repo, env, tmp.toString());

        var health = ind.health();
        assertThat(health.getStatus().getCode()).isEqualTo("UP");
        Map<String, Object> details = health.getDetails();
        assertThat(details).containsKey("uptime");
        assertThat(((Map<?,?>)details.get("storage")).get("path").toString()).contains(tmp.toString());
        assertThat(((Map<?,?>)details.get("database")).get("products")).isEqualTo(3L);
    }

    @Test
    void health_down_when_storage_path_invalid() {
        var repo = Mockito.mock(se.moln.productservice.repository.ProductRepository.class);
        Mockito.when(repo.count()).thenReturn(1L);
        Environment env = Mockito.mock(Environment.class);
        Mockito.when(env.getProperty("spring.application.name", "product-service")).thenReturn("product-service");
        Mockito.when(env.getActiveProfiles()).thenReturn(new String[]{});

        // Use a universally invalid path (contains NUL) to provoke an exception inside storage section on all OS
        String invalidPath = "bad\u0000path";
        AppHealthIndicator ind = new AppHealthIndicator(repo, env, invalidPath);

        var health = ind.health();
        assertThat(health.getStatus().getCode()).isEqualTo("DOWN");
    }

    @Test
    void health_down_when_db_throws() throws IOException {
        ProductRepository repo = Mockito.mock(ProductRepository.class);
        Mockito.when(repo.count()).thenThrow(new RuntimeException("DB down"));
        Environment env = Mockito.mock(Environment.class);
        Mockito.when(env.getProperty("spring.application.name", "product-service")).thenReturn("product-service");
        Mockito.when(env.getActiveProfiles()).thenReturn(new String[]{});

        Path tmp = Files.createTempDirectory("uploads-test");
        AppHealthIndicator ind = new AppHealthIndicator(repo, env, tmp.toString());

        var health = ind.health();
        assertThat(health.getStatus().getCode()).isEqualTo("DOWN");
        assertThat(health.getDetails()).containsKey("database");
    }
}