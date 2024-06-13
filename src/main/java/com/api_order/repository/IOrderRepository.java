package com.api_order.repository;

import com.api_order.model.Ordenes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrderRepository extends JpaRepository<Ordenes,Integer> {
}
