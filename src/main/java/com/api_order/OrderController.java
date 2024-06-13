package com.api_order;

import com.api_order.model.Ordenes;
import com.api_order.repository.IOrderRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    @Autowired
    IOrderRepository iOrderRepository;

    //Crear nueva orden
    @PostMapping
    public Ordenes createOrder(@RequestBody Ordenes ordenes){
        return iOrderRepository.save(ordenes);
    }


    //Obtener todas las ordenes
    @GetMapping("/orders")
    public List<Ordenes> getAllOrders(){
        return iOrderRepository.findAll();
    }

    //Obtener una orden por ID
    @GetMapping("/orders/{id}")
    public ResponseEntity<Ordenes>getOrderById(@PathVariable Integer id){
        Ordenes orden = iOrderRepository.findById(id).orElse(null);
        if(orden == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orden);
    }

    //Actualizar una orden
    @PutMapping("/orders/{id}")
    public ResponseEntity<Ordenes>updateOrderById(@PathVariable Integer id, @RequestBody Ordenes orderDetails){
        Ordenes orden = iOrderRepository.findById(id).orElse(null);
        if(orden == null){
            return ResponseEntity.notFound().build();
        }
        orden.setProduct(orderDetails.getProduct());
        orden.setQuantity(orderDetails.getQuantity());
        orden.setOrderDate(orderDetails.getOrderDate());
        orden.setTotalPrice(orderDetails.getTotalPrice());
        Ordenes newOrder = iOrderRepository.save(orden);
        return ResponseEntity.ok(newOrder);
    }


    //Eliminar una orden
    @DeleteMapping("/orders/{id}")
    public ResponseEntity<Void>deleteOrderById(@PathVariable Integer id){
        Ordenes orden = iOrderRepository.findById(id).orElse(null);
        if(orden == null){
            return ResponseEntity.notFound().build();
        }
        iOrderRepository.delete(orden);
        return ResponseEntity.noContent().build();
    }
}
