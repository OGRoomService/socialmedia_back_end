package com.mantarays.socialbackend.controller;

import com.mantarays.socialbackend.data.ToDo;
import com.mantarays.socialbackend.service.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/todo")
public class ToDoController {
    @Autowired
    private ToDoService toDoService;

    @GetMapping
    public Iterable<ToDo> findAll() {
        return toDoService.findAll();
    }

    @GetMapping("/{id}")
    public ToDo findById(@PathVariable Long id) {
        return toDoService.findById(id);
    }

    @PostMapping ToDo create(@RequestBody ToDo toDo) {
        return toDoService.save(toDo);
    }

    @PutMapping("/{id}")
    public ToDo update(@RequestBody ToDo toDo) {
        return toDoService.save(toDo);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        toDoService.deleteById(id);
    }
}
