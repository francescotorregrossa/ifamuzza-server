package com.ifamuzza.ingegneriadelsoftware.repository;

import com.ifamuzza.ingegneriadelsoftware.model.users.User;

import org.springframework.data.repository.CrudRepository;;

public interface UserRepository extends CrudRepository<User, Integer> { }