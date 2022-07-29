package com.nikita.todo.controller;

import com.nikita.todo.business.service.impl.TodoTodoTaskServiceImpl;
import com.nikita.todo.model.TodoTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/todo")
public class TodoTaskController {
    private final TodoTodoTaskServiceImpl service;
    @Autowired
    public TodoTaskController(TodoTodoTaskServiceImpl service) {
        this.service = service;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TodoTask> getTaskById(@PathVariable Long id) {
        Optional<TodoTask> task = service.findTaskById(id);
        return task.map(value -> new ResponseEntity<>(value, HttpStatus.FOUND))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @GetMapping
    ResponseEntity<List<TodoTask>> getAllTasks() {
        List<TodoTask> list = service.findAllTasks();
        return !list.isEmpty() ? new ResponseEntity<>(list, HttpStatus.FOUND) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    ResponseEntity<TodoTask> addTask(@Valid @RequestBody TodoTask newTodoTask,
                                     BindingResult result) {
        if (newTodoTask.getId() != null || result.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(service.addTask(newTodoTask), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    ResponseEntity<TodoTask> editTaskById(@PathVariable Long id,
                                          @Valid @RequestBody TodoTask newTodoTask,
                                          BindingResult bindingResult) {
        if (!id.equals(newTodoTask.getId()) || bindingResult.hasErrors())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (!service.findTaskById(newTodoTask.getId()).isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        service.editTask(newTodoTask);
        return new ResponseEntity<>(newTodoTask, HttpStatus.OK);
    }

}