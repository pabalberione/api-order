package com.api_order.service;

import com.api_order.exceptions.ResourceNotFoundException;
import com.api_order.model.Ordenes;
import com.api_order.repository.IOrderRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    IOrderRepository iOrderRepository;

    @InjectMocks
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

        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetOrderById_Found(){
        //Configurar datos de la prueba
        when(iOrderRepository.findById(1)).thenReturn(Optional.of(orderCreate));

        //Llamar al método a probar
        Ordenes result = orderService.getOrderById(1);

        //Verificar resultados
        assertEquals(orderCreate, result);
    }

    @Test
    void testGetOrderById_NotFound(){
        //Configurar datos de la prueba
        when(iOrderRepository.findById(anyInt())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, ()->{
            orderService.getOrderById(1);
        });

        //Verificar resultados
        assertEquals("La orden con id 1 no pudo ser encontrada",exception.getMessage());
    }

    @Test
    void testCreateOrder(){
        Ordenes orden = new Ordenes(1,"Almohadas verdes",4,28000.0,"2023-06-13T10:15:30.000+00:00");
        //Configuración de datos de prueba
        when(iOrderRepository.save(orden)).thenReturn(orderCreate);

        //Llamar al método a probar
        Ordenes result = orderService.createOrder(orden);

        //Verificar resultados
        assertNotNull(result);
        assertEquals(orderCreate.getId(),orden.getId());
        assertEquals(orderCreate.getProduct(),orden.getProduct());
        assertEquals(orderCreate.getQuantity(),orden.getQuantity());
        assertEquals(orderCreate.getTotalPrice(),orden.getTotalPrice());
        assertEquals(orderCreate.getOrderDate(),orden.getOrderDate());
    }

    @Test
    void testDeleteOrder_NotFound(){
        //Configurar el mock para que findById devuelva vacío
        when(iOrderRepository.findById(1)).thenReturn(Optional.empty());
        //Verificar que se lanza una ResourceNotFoundException exception
        assertThrows(ResourceNotFoundException.class,()->orderService.deleteOrder(1));
    }

    @Test
    void testDeleteOrder(){
        //Configurar el mock para que se devuelva una orden
        when(iOrderRepository.findById(1)).thenReturn(Optional.of(orderCreate));

        //Ejecutar el metodo a probar
        orderService.deleteOrder(1);

        //Verificar que se haya llamado al metodo delete del repositorio solo una vez
        verify(iOrderRepository, times(1)).delete(orderCreate);
    }

}
