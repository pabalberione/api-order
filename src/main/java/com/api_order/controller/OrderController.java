package com.api_order.controller;

import com.api_order.model.Ordenes;
import com.api_order.repository.IOrderRepository;
import com.api_order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    //Crear nueva orden
    @PostMapping
    public ResponseEntity<Ordenes>crearOrden(@RequestBody Ordenes ordenes){
        Ordenes orden = orderService.createOrder(ordenes);
        return ResponseEntity.status(HttpStatus.CREATED).body(orden);
    }


    //Obtener todas las ordenes
    @GetMapping()
    public ResponseEntity<List<Ordenes>> getAllOrders(){
        List<Ordenes>ordenes = orderService.getAllOrders();
        return ResponseEntity.ok(ordenes);
    }

    //Obtener una orden por ID
    @GetMapping("/{id}")
    public ResponseEntity<Ordenes>getOrderById(@PathVariable Integer id){
        Ordenes orden = orderService.getOrderById(id);
        return ResponseEntity.ok(orden);
    }

    //Actualizar una orden
    @PutMapping("/{id}")
    public ResponseEntity<Ordenes>updateOrderById(@PathVariable Integer id, @RequestBody Ordenes orderDetails){
        Ordenes orden = orderService.updateOrder(id, orderDetails);
        return ResponseEntity.ok(orden);
    }


    //Eliminar una orden
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteOrderById(@PathVariable Integer id){
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
