package com.learner20.javacompletablefuture.repository;

import com.learner20.javacompletablefuture.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
