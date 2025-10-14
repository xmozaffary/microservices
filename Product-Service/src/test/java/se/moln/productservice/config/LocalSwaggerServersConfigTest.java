package se.moln.productservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.junit.jupiter.api.Test;
import org.springdoc.core.customizers.OpenApiCustomizer;

import static org.assertj.core.api.Assertions.*;

class LocalSwaggerServersConfigTest {

    @Test
    void localServers_ShouldCustomizeServers_ForGivenPort() {
        LocalSwaggerServersConfig cfg = new LocalSwaggerServersConfig();
        OpenApiCustomizer customizer = cfg.localServers(9090);

        OpenAPI api = new OpenAPI();
        customizer.customise(api);

        assertThat(api.getServers()).isNotNull();
        assertThat(api.getServers())
                .extracting(Server::getUrl)
                .containsExactly("http://localhost:9090");
        assertThat(api.getServers().get(0).getDescription()).contains("Local");
    }
}
