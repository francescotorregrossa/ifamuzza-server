package com.ifamuzza.ingegneriadelsoftware.repository;

import com.ifamuzza.ingegneriadelsoftware.model.payment.Method;
import org.springframework.data.repository.CrudRepository;

public interface MethodRepository extends CrudRepository<Method, Integer> { }