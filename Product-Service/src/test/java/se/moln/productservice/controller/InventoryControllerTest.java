package se.moln.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import se.moln.productservice.dto.AdjustStockRequest;
import se.moln.productservice.dto.InventoryResponse;
import se.moln.productservice.dto.ProductResponse;
import se.moln.productservice.dto.PurchaseResponse;
import se.moln.productservice.model.StockStatus;
import se.moln.productservice.service.InventoryService;

import java.util.UUID;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventoryController.class)
@AutoConfigureMockMvc(addFilters = false)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private InventoryService inventoryService;

    private UUID productId;
    private InventoryResponse inventoryResponse;
    private ProductResponse productResponse;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        inventoryResponse = new InventoryResponse(productId, 5, "OK");
        productResponse = new ProductResponse(
                productId,
                "Test Product",
                "test-product",
                "desc",
                BigDecimal.valueOf(10),
                "SEK",
                "Electronics",
                5,
                true,
                new HashMap<>(),
                List.of()
        );
    }

    @Test
    void purchase_InsufficientStock_ShouldReturnConflictProblem() throws Exception {
        se.moln.productservice.exception.InsufficientStockException ex =
                new se.moln.productservice.exception.InsufficientStockException(productId, 5, 2, "Otillräckligt lager");
        when(inventoryService.purchase(eq(productId), eq(5))).thenThrow(ex);

        AdjustStockRequest req = new AdjustStockRequest(5);
        mockMvc.perform(post("/api/inventory/{productId}/purchase", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict())
                .andExpect(header().string("Content-Type", org.hamcrest.Matchers.containsString("application/problem+json")))
                .andExpect(jsonPath("$.title").value("Otillräckligt lager"))
                .andExpect(jsonPath("$.productId").value(productId.toString()));
    }

    @Test
    void purchase_ShouldReturnUpdatedInventory() throws Exception {
        when(inventoryService.purchase(eq(productId), eq(3)))
                .thenReturn(new PurchaseResponse(productResponse, 3, 2, "Purchase successful."));

        AdjustStockRequest req = new AdjustStockRequest(3);
        mockMvc.perform(post("/api/inventory/{productId}/purchase", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.purchasedQuantity").value(3))
                .andExpect(jsonPath("$.newStockQuantity").value(2))
                .andExpect(jsonPath("$.product.id").value(productId.toString()));
    }

    @Test
    void return_ShouldReturnUpdatedInventory() throws Exception {
        when(inventoryService.refund(eq(productId), eq(4))).thenReturn(new InventoryResponse(productId, 9, "Return successful."));

        AdjustStockRequest req = new AdjustStockRequest(4);
        mockMvc.perform(post("/api/inventory/{productId}/return", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(9))
                .andExpect(jsonPath("$.message").value("Return successful."));
    }

    @Test
    void purchase_WithInvalidQuantity_ShouldReturnBadRequest() throws Exception {
        // @Min(1) on AdjustStockRequest.quantity should trigger validation 400
        AdjustStockRequest req = new AdjustStockRequest(0);
        mockMvc.perform(post("/api/inventory/{productId}/purchase", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void return_WithInvalidQuantity_ShouldReturnBadRequest() throws Exception {
        AdjustStockRequest req = new AdjustStockRequest(0);
        mockMvc.perform(post("/api/inventory/{productId}/return", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }
}