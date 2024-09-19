package com.api_order.service;

import com.api_order.exceptions.ResourceNotFoundException;
import com.api_order.model.Ordenes;
import com.api_order.repository.IOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    IOrderRepository iOrderRepository;

    //Obtener todas las ordenes
    public List<Ordenes> getAllOrders(){
        return iOrderRepository.findAll();
    }

    //Obtener orden por ID
    public Ordenes getOrderById(Integer id){
        return iOrderRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("La orden con id "+ id + " no pudo ser encontrada"));
    }

    //Crear nueva orden
    public Ordenes createOrder(Ordenes orden){
        return iOrderRepository.save(orden);
    }

    //Modificar orden
    public Ordenes updateOrder(Integer id, Ordenes ordenDetalle){
        Ordenes ordenActual = iOrderRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Orden con id " + " no pudo ser encontrada"));

        ordenActual.setProduct(ordenDetalle.getProduct());
        ordenActual.setQuantity(ordenDetalle.getQuantity());
        ordenActual.setTotalPrice(ordenDetalle.getTotalPrice());
        ordenActual.setOrderDate(ordenDetalle.getOrderDate());
        return iOrderRepository.save(ordenActual);
    }
    //Eliminar orden
    public void deleteOrder(Integer id){
        Ordenes orden = iOrderRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Orden con id " + id + " no pudo ser encontrada"));
        iOrderRepository.delete(orden);
    }
}
