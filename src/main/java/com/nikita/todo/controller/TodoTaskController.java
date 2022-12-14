package com.nikita.todo.controller;

import com.nikita.todo.business.service.impl.TodoTaskServiceImpl;
import com.nikita.todo.model.TodoTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(value = "/api/v1")
public class TodoTaskController {
    private final TodoTaskServiceImpl service;

    @Autowired
    public TodoTaskController(TodoTaskServiceImpl service) {
        this.service = service;
    }

    @GetMapping(value = "/todos/{id}")
    public ResponseEntity<TodoTask> getTaskById(@PathVariable Long id) {
        Optional<TodoTask> task = service.findTaskById(id);
        return task.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @GetMapping("/todos")
    ResponseEntity<List<TodoTask>> getAllTasks() {
        List<TodoTask> list = service.findAllTasks();
        return !list.isEmpty() ? new ResponseEntity<>(list, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/todos")
    ResponseEntity<TodoTask> addTask(@Valid @RequestBody TodoTask newTodoTask,
                                     BindingResult result) {
        if (newTodoTask.getId() != null || result.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(service.addTask(newTodoTask), HttpStatus.CREATED);
    }

    @PutMapping("/todos/{id}")
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

    @DeleteMapping("/todos/{id}")
    ResponseEntity<TodoTask> deleteTaskById(@PathVariable Long id) {
        if (!service.findTaskById(id).isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        service.deleteTaskById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);


    }

}
