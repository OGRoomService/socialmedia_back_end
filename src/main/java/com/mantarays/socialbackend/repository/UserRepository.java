package com.mantarays.socialbackend.repository;

import com.mantarays.socialbackend.data.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    
}
