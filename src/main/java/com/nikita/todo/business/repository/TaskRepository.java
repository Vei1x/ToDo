package com.nikita.todo.business.repository;

import com.nikita.todo.business.repository.model.TodoTaskDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TodoTaskDao, Long> {
}
