package com.mantarays.socialbackend.repository;

import com.mantarays.socialbackend.data.ToDo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToDoRepository extends CrudRepository<ToDo, String>{
    
}
