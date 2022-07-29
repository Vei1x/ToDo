package com.nikita.todo.business.mapper;

import com.nikita.todo.business.repository.model.TodoTaskDao;
import com.nikita.todo.model.TodoTask;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TodoTaskDao toDAO(TodoTask todoTask);
    TodoTask fromDAO(TodoTaskDao todoTaskDao);
}
