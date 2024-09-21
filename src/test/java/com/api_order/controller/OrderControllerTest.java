package com.api_order.controller;

import com.api_order.exceptions.ResourceNotFoundException;
import com.api_order.model.Ordenes;
import com.api_order.service.OrderService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(OrderController.class)
@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Ordenes orderCreate;
    private Ordenes orderUpdate;
    private List<Ordenes> orderList;

    @BeforeEach
    void setUp() throws Exception{
        //Cargar los datos de los archivos Json
        orderCreate = objectMapper.readValue(
                Files.readAllBytes(Paths.get("src/test/resources/order-create.json")),Ordenes.class);
        orderUpdate = objectMapper.readValue(
                Files.readAllBytes(Paths.get("src/test/resources/order-update.json")),Ordenes.class);
        orderList = objectMapper.readValue(
                Files.readAllBytes(Paths.get("src/test/resources/order-list.json")),
                new TypeReference<List<Ordenes>>() {});
    }

    @Test
    void testCreateOrder()throws Exception{
        when(orderService.createOrder(any(Ordenes.class))).thenReturn(orderCreate);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderCreate)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.product").value("Almohadas verdes"))
                .andExpect(jsonPath("$.quantity").value(4))
                .andExpect(jsonPath("$.totalPrice").value(28000.0))
                .andExpect(jsonPath("$.orderDate").value("2023-06-13T10:15:30.000+00:00"));
    }

    @Test
    void testGetOrderById()throws Exception{
        when(orderService.getOrderById(anyInt())).thenReturn(orderCreate);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/order/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.product").value("Almohadas verdes"))
                .andExpect(jsonPath("$.quantity").value(4))
                .andExpect(jsonPath("$.totalPrice").value(28000.0))
                .andExpect(jsonPath("$.orderDate").value("2023-06-13T10:15:30.000+00:00"));
    }

    @Test
    void testGetOrderByIdNotFound()throws Exception {
        when(orderService.getOrderById(1)).thenThrow(new ResourceNotFoundException("La orden con id 1 no pudo ser encontrada"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/order/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllOrders()throws Exception{
        when(orderService.getAllOrders()).thenReturn(orderList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/order")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].product").value("Almohadas Rojas"))
                .andExpect(jsonPath("$[0].quantity").value(3))
                .andExpect(jsonPath("$[0].totalPrice").value(22000.0))
                .andExpect(jsonPath("$[0].orderDate").value("2023-06-13T10:15:30.000+00:00"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].product").value("Almohadas"))
                .andExpect(jsonPath("$[1].quantity").value(6))
                .andExpect(jsonPath("$[1].totalPrice").value(25000.0))
                .andExpect(jsonPath("$[1].orderDate").value("2023-06-13T10:15:30.000+00:00"));

    }

    @Test
    void testUpdateOrder()throws Exception{
        when(orderService.updateOrder(anyInt(),any(Ordenes.class))).thenReturn(orderUpdate);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/order/1")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderUpdate)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.product").value("Almohadas verdes modificadas"))
                .andExpect(jsonPath("$.quantity").value(4))
                .andExpect(jsonPath("$.totalPrice").value(28000.0))
                .andExpect(jsonPath("$.orderDate").value("2023-06-13T10:15:30.000+00:00"));
    }

    @Test
    void testDeleteOrder()throws Exception{
        doNothing().when(orderService).deleteOrder(anyInt());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/order/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
