package com.ifamuzza.ingegneriadelsoftware.repository;

import com.ifamuzza.ingegneriadelsoftware.model.payment.PaymentMethod;

import org.springframework.data.repository.CrudRepository;;

public interface PaymentMethodRepository extends CrudRepository<PaymentMethod, Integer> { }