package com.mantarays.socialbackend.service;

//import java.util.List;

import com.mantarays.socialbackend.data.ToDo;
import com.mantarays.socialbackend.exception.EntityNotFoundException;
import com.mantarays.socialbackend.repository.ToDoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ToDoService {
    @Autowired
    private ToDoRepository toDoRepository;

    public Iterable<ToDo> findAll() {
        return toDoRepository.findAll();
    }

    public ToDo findById(String id) {
        return toDoRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public ToDo save(ToDo toDo) {
        return toDoRepository.save(toDo);
    }

    public void deleteById(String id) {
        toDoRepository.deleteById(id);
    }
}
