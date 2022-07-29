package com.nikita.todo.business.service.impl;

import com.nikita.todo.business.mapper.TaskMapper;
import com.nikita.todo.business.repository.TaskRepository;
import com.nikita.todo.business.repository.model.TodoTaskDao;
import com.nikita.todo.business.service.TaskService;
import com.nikita.todo.model.TodoTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    final TaskMapper mapper;
    final TaskRepository repository;

    @Autowired
    public TaskServiceImpl(TaskMapper mapper, TaskRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    public List<TodoTask> getAllTasks() {
        List<TodoTaskDao> list = repository.findAll();
        return list.stream().map(mapper::fromDAO).collect(Collectors.toList());
    }

    @Override
    public Optional<TodoTask> getTaskById(Long id) {
        return repository.findById(id).map(mapper::fromDAO);
    }

    @Override
    public void deleteTaskById(Long id) {

    }

    @Override
    public TodoTask addTask(TodoTask todoTask) {
        TodoTaskDao taskToAdd = repository.save(mapper.toDAO(todoTask));
        return mapper.fromDAO(taskToAdd);
    }

    @Override
    public TodoTask editTask(TodoTask newTodoTask) {
        TodoTask oldTodoTask = mapper.fromDAO(repository.getReferenceById(newTodoTask.getId()));
        oldTodoTask.setName(newTodoTask.getName());
        oldTodoTask.setDescription(newTodoTask.getDescription());
        return mapper.fromDAO(repository.save(mapper.toDAO(oldTodoTask)));

    }
}
