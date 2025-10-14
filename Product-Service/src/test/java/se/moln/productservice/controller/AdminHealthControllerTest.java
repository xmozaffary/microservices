package se.moln.productservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import({org.springframework.boot.actuate.autoconfigure.endpoint.jackson.JacksonEndpointAutoConfiguration.class,
        AdminHealthControllerTest.ActuatorTestConfig.class})
class AdminHealthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HealthEndpoint healthEndpoint;

    @Autowired
    private InfoEndpoint infoEndpoint;

    @TestConfiguration
    static class ActuatorTestConfig {
        @Bean
        HealthEndpoint healthEndpoint() {
            return mock(HealthEndpoint.class);
        }

        @Bean
        InfoEndpoint infoEndpoint() {
            return mock(InfoEndpoint.class);
        }
    }

    @Test
    void healthDetails_Unauthorized_WhenNotAdmin() throws Exception {
        mockMvc.perform(get("/api/admin/health-details"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void healthDetails_Forbidden_WhenNotAdminRole() throws Exception {
        mockMvc.perform(get("/api/admin/health-details"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void healthDetails_ReturnsDetails_WhenAdmin() throws Exception {
        when(healthEndpoint.health()).thenReturn(Health.status(Status.UP).build());
        when(infoEndpoint.info()).thenReturn(java.util.Map.of("app", java.util.Map.of("name", "product-service")));

        ObjectProvider<HealthEndpoint> healthProvider = new ObjectProvider<>() {
            @Override
            public HealthEndpoint getObject(Object... args) { return healthEndpoint; }
            @Override
            public HealthEndpoint getIfAvailable() { return healthEndpoint; }
            @Override
            public HealthEndpoint getIfUnique() { return healthEndpoint; }
            @Override
            public void forEach(Consumer action) { action.accept(healthEndpoint); }
            @Override
            public Stream<HealthEndpoint> stream() { return Stream.of(healthEndpoint); }
            @Override
            public Iterator<HealthEndpoint> iterator() { return Stream.of(healthEndpoint).iterator(); }
            @Override
            public Spliterator<HealthEndpoint> spliterator() { return Stream.of(healthEndpoint).spliterator(); }
        };

        ObjectProvider<InfoEndpoint> infoProvider = new ObjectProvider<>() {
            @Override
            public InfoEndpoint getObject(Object... args) { return infoEndpoint; }
            @Override
            public InfoEndpoint getIfAvailable() { return infoEndpoint; }
            @Override
            public InfoEndpoint getIfUnique() { return infoEndpoint; }
            @Override
            public void forEach(Consumer action) { action.accept(infoEndpoint); }
            @Override
            public Stream<InfoEndpoint> stream() { return Stream.of(infoEndpoint); }
            @Override
            public Iterator<InfoEndpoint> iterator() { return Stream.of(infoEndpoint).iterator(); }
            @Override
            public Spliterator<InfoEndpoint> spliterator() { return Stream.of(infoEndpoint).spliterator(); }
        };

        MockMvc standaloneMvc = MockMvcBuilders
                .standaloneSetup(new AdminHealthController(healthProvider, infoProvider))
                .build();

        standaloneMvc.perform(get("/api/admin/health-details"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.info.app.name").value("product-service"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void healthDetails_ReturnsUnknown_WhenEndpointsUnavailable() throws Exception {
        ObjectProvider<HealthEndpoint> nullHealthProvider = new ObjectProvider<>() {
            @Override
            public HealthEndpoint getObject(Object... args) { return null; }
            @Override
            public HealthEndpoint getIfAvailable() { return null; }
            @Override
            public HealthEndpoint getIfUnique() { return null; }
            @Override
            public void forEach(java.util.function.Consumer action) {}
            @Override
            public java.util.stream.Stream<HealthEndpoint> stream() { return java.util.stream.Stream.empty(); }
            @Override
            public java.util.Iterator<HealthEndpoint> iterator() { return java.util.List.<HealthEndpoint>of().iterator(); }
            @Override
            public java.util.Spliterator<HealthEndpoint> spliterator() { return java.util.List.<HealthEndpoint>of().spliterator(); }
        };

        ObjectProvider<InfoEndpoint> nullInfoProvider = new ObjectProvider<>() {
            @Override
            public InfoEndpoint getObject(Object... args) { return null; }
            @Override
            public InfoEndpoint getIfAvailable() { return null; }
            @Override
            public InfoEndpoint getIfUnique() { return null; }
            @Override
            public void forEach(java.util.function.Consumer action) {}
            @Override
            public java.util.stream.Stream<InfoEndpoint> stream() { return java.util.stream.Stream.empty(); }
            @Override
            public java.util.Iterator<InfoEndpoint> iterator() { return java.util.List.<InfoEndpoint>of().iterator(); }
            @Override
            public java.util.Spliterator<InfoEndpoint> spliterator() { return java.util.List.<InfoEndpoint>of().spliterator(); }
        };

        MockMvc mvc = MockMvcBuilders
                .standaloneSetup(new AdminHealthController(nullHealthProvider, nullInfoProvider))
                .build();

        mvc.perform(get("/api/admin/health-details"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UNKNOWN"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void healthDetails_HealthAvailable_InfoMissing_ShouldReturnUnknown() throws Exception {
        ObjectProvider<HealthEndpoint> healthProvider = new ObjectProvider<>() {
            @Override public HealthEndpoint getObject(Object... args) { return healthEndpoint; }
            @Override public HealthEndpoint getIfAvailable() { return healthEndpoint; }
            @Override public HealthEndpoint getIfUnique() { return healthEndpoint; }
            @Override public void forEach(java.util.function.Consumer action) { action.accept(healthEndpoint); }
            @Override public java.util.stream.Stream<HealthEndpoint> stream() { return java.util.stream.Stream.of(healthEndpoint); }
            @Override public java.util.Iterator<HealthEndpoint> iterator() { return java.util.List.of(healthEndpoint).iterator(); }
            @Override public java.util.Spliterator<HealthEndpoint> spliterator() { return java.util.List.<HealthEndpoint>of(healthEndpoint).spliterator(); }
        };

        ObjectProvider<InfoEndpoint> nullInfoProvider = new ObjectProvider<>() {
            @Override public InfoEndpoint getObject(Object... args) { return null; }
            @Override public InfoEndpoint getIfAvailable() { return null; }
            @Override public InfoEndpoint getIfUnique() { return null; }
            @Override public void forEach(java.util.function.Consumer action) {}
            @Override public java.util.stream.Stream<InfoEndpoint> stream() { return java.util.stream.Stream.empty(); }
            @Override public java.util.Iterator<InfoEndpoint> iterator() { return java.util.List.<InfoEndpoint>of().iterator(); }
            @Override public java.util.Spliterator<InfoEndpoint> spliterator() { return java.util.List.<InfoEndpoint>of().spliterator(); }
        };

        MockMvc mvc = MockMvcBuilders
                .standaloneSetup(new AdminHealthController(healthProvider, nullInfoProvider))
                .build();

        mvc.perform(get("/api/admin/health-details"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UNKNOWN"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void healthDetails_InfoAvailable_HealthMissing_ShouldReturnUnknown() throws Exception {
        ObjectProvider<HealthEndpoint> nullHealthProvider = new ObjectProvider<>() {
            @Override public HealthEndpoint getObject(Object... args) { return null; }
            @Override public HealthEndpoint getIfAvailable() { return null; }
            @Override public HealthEndpoint getIfUnique() { return null; }
            @Override public void forEach(java.util.function.Consumer action) {}
            @Override public java.util.stream.Stream<HealthEndpoint> stream() { return java.util.stream.Stream.empty(); }
            @Override public java.util.Iterator<HealthEndpoint> iterator() { return java.util.List.<HealthEndpoint>of().iterator(); }
            @Override public java.util.Spliterator<HealthEndpoint> spliterator() { return java.util.List.<HealthEndpoint>of().spliterator(); }
        };

        ObjectProvider<InfoEndpoint> infoProvider = new ObjectProvider<>() {
            @Override public InfoEndpoint getObject(Object... args) { return infoEndpoint; }
            @Override public InfoEndpoint getIfAvailable() { return infoEndpoint; }
            @Override public InfoEndpoint getIfUnique() { return infoEndpoint; }
            @Override public void forEach(java.util.function.Consumer action) { action.accept(infoEndpoint); }
            @Override public java.util.stream.Stream<InfoEndpoint> stream() { return java.util.stream.Stream.of(infoEndpoint); }
            @Override public java.util.Iterator<InfoEndpoint> iterator() { return java.util.List.of(infoEndpoint).iterator(); }
            @Override public java.util.Spliterator<InfoEndpoint> spliterator() { return java.util.List.<InfoEndpoint>of(infoEndpoint).spliterator(); }
        };

        MockMvc mvc = MockMvcBuilders
                .standaloneSetup(new AdminHealthController(nullHealthProvider, infoProvider))
                .build();

        mvc.perform(get("/api/admin/health-details"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UNKNOWN"));
    }
}