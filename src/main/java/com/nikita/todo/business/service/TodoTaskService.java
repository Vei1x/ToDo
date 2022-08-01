package com.nikita.todo.business.service;

import com.nikita.todo.model.TodoTask;

import java.util.List;
import java.util.Optional;

public interface TodoTaskService {
    List<TodoTask> findAllTasks();
    Optional<TodoTask> findTaskById(Long id);

    void deleteTaskById(Long id);

    TodoTask addTask(TodoTask todoTask);

    TodoTask editTask(TodoTask newTodoTask);
}
