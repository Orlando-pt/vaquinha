package com.orlando.vaquinha.repository;

import java.sql.Timestamp;
import java.util.Arrays;

import com.orlando.vaquinha.model.Payment;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class PaymentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PaymentRepository paymentRepository;

    private Payment payment1;
    private Payment payment2;

    @BeforeEach
    void setUp() {
        payment1 = new Payment();
        payment1.setName("Orlando");
        payment1.setAmount(0.62);
        payment1.setTimestamp(
            Timestamp.valueOf("2021-08-14 16:00:00")
        );


        payment2 = new Payment();
        payment2.setName("Moura");
        payment2.setAmount(10.0);
        payment2.setTimestamp(
            Timestamp.valueOf("2021-08-15 00:00:00")
        );

        this.entityManager.persist(payment1);
        this.entityManager.persist(payment2);
        this.entityManager.flush();
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void testFindPaymentsByName() {
        var res = this.paymentRepository.findAllByName(payment1.getName());

        Assertions.assertThat(res).
            hasSize(1).containsOnly(this.payment1);
    }

    @Test
    void testFindPaymentsByPaymentTimestampAfter() {
        var res = this.paymentRepository.findAllByTimestampAfter(
            Timestamp.valueOf("2021-08-14 17:00:00")
        );

        Assertions.assertThat(res).
            hasSize(1).containsOnly(payment2);

        var resWithBoth = this.paymentRepository.findAllByTimestampAfter(
            Timestamp.valueOf("2021-08-14 10:00:00")
        );

        var payments = Arrays.asList(
            payment1, payment2
        );
        Assertions.assertThat(resWithBoth).
            hasSize(2).containsAll(payments);
    }
}
