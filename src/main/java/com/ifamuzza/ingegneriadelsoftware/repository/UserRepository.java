package com.ifamuzza.ingegneriadelsoftware.repository;

import com.ifamuzza.ingegneriadelsoftware.model.User;

import org.springframework.data.repository.CrudRepository;;

public interface UserRepository extends CrudRepository<User, Integer> { }