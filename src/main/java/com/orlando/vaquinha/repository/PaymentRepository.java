package com.orlando.vaquinha.repository;

import java.sql.Timestamp;

import com.orlando.vaquinha.model.Payment;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>{
    
    List<Payment> findAllByTimestampAfter(Timestamp timestamp);
    List<Payment> findAllByName(String name);
    List<Payment> findTop12ByOrderByTimestampDesc();
    List<Payment> findTop12ByName(String name, Sort sort);
}
