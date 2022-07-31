package com.nikita.todo.business.service.impl;

import com.nikita.todo.business.mapper.TodoTaskMapper;
import com.nikita.todo.business.repository.TodoTaskRepository;
import com.nikita.todo.business.repository.model.TodoTaskDao;
import com.nikita.todo.business.service.TodoTaskService;
import com.nikita.todo.model.TodoTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TodoTaskServiceImpl implements TodoTaskService {
    final TodoTaskMapper mapper;
    final TodoTaskRepository repository;

    @Autowired
    public TodoTaskServiceImpl(TodoTaskMapper mapper, TodoTaskRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    public List<TodoTask> findAllTasks() {
        List<TodoTaskDao> list = repository.findAll();
        return list.stream().map(mapper::fromDAO).collect(Collectors.toList());
    }

    @Override
    public Optional<TodoTask> findTaskById(Long id) {
        return repository.findById(id).map(mapper::fromDAO);
    }

    @Override
    public void deleteTaskById(Long id) {
       repository.deleteById(id);
    }

    @Override
    public TodoTask addTask(TodoTask todoTask) {
        TodoTaskDao taskToAdd = repository.save(mapper.toDAO(todoTask));
        return mapper.fromDAO(taskToAdd);
    }

    @Override
    public TodoTask editTask(TodoTask newTodoTask) {
        TodoTaskDao todoTaskDao = repository.save(mapper.toDAO(newTodoTask));
        return mapper.fromDAO(todoTaskDao);

    }
}
