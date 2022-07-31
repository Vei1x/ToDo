package com.nikita.todo.business.service.impl;

import com.nikita.todo.business.mapper.TodoTaskMapper;
import com.nikita.todo.business.repository.TodoTaskRepository;
import com.nikita.todo.business.repository.model.TodoTaskDao;
import com.nikita.todo.model.TodoTask;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class TodoTaskServiceImplTest {

    @Mock
    private TodoTaskRepository repository;

    @Mock
    private TodoTaskMapper mapper;

    @InjectMocks
    private TodoTaskServiceImpl service;

    private TodoTask todoTask;
    private TodoTaskDao todoTaskDao;

    private List<TodoTaskDao> todoTaskDaoList;


    @BeforeEach
    public void init() {
        todoTask = createTodoTask();
        todoTaskDao = createTodoTaskDao();
        todoTaskDaoList = createTodoTaskDaoList(todoTaskDao);
    }

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllTasks() {
        when(repository.findAll()).thenReturn(todoTaskDaoList);
        when(mapper.fromDAO(todoTaskDao)).thenReturn(todoTask);

        List<TodoTask> testList = service.findAllTasks();

        assertEquals(2, testList.size());

        verify(repository, times(1)).findAll();
    }

    @Test
    void getAllTasksEmpty() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        Assertions.assertTrue(service.findAllTasks().isEmpty());
        verify(repository, times(1)).findAll();

    }

    @Test
    void getTaskById() {
        when(repository.findById(todoTask.getId())).thenReturn(Optional.of(todoTaskDao));
        when(mapper.fromDAO(todoTaskDao)).thenReturn(todoTask);
        Optional<TodoTask> optionalTodoTask = service.findTaskById(todoTask.getId());
        assertEquals(todoTask.getId(), optionalTodoTask.get().getId());
        assertEquals(todoTask.getId(), optionalTodoTask.get().getId());
    }

    @Test
    void getTaskByIdButEmpty() {
        when(repository.findById(any())).thenReturn(Optional.empty());
        Optional<TodoTask> optionalTodoTask = service.findTaskById(any());
        assertFalse(optionalTodoTask.isPresent());
    }


    @Test
    void addTask() {
        when(repository.save(todoTaskDao)).thenReturn(todoTaskDao);
        when(mapper.fromDAO(todoTaskDao)).thenReturn(todoTask);
        when(mapper.toDAO(todoTask)).thenReturn(todoTaskDao);

        TodoTask savedInternStaffing = service.addTask(todoTask);
        assertEquals(todoTask, savedInternStaffing);
        verify(repository, times(1)).save(todoTaskDao);

    }

    @Test
    void editTask() {
        when(repository.save(todoTaskDao)).thenReturn(todoTaskDao);
        when(mapper.toDAO(todoTask)).thenReturn(todoTaskDao);
        when(mapper.fromDAO(todoTaskDao)).thenReturn(todoTask);

        TodoTask task = service.editTask(todoTask);
        assertEquals(todoTask.getId(), task.getId());
        verify(repository, times(1)).save(todoTaskDao);
    }
    @Test
    void editWrongTask() {
        when(repository.save(todoTaskDao)).thenThrow(new IllegalArgumentException());
        when(mapper.toDAO(todoTask)).thenReturn(todoTaskDao);
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.editTask(todoTask));
        verify(repository, times(1)).save(todoTaskDao);
    }

    @Test
    void deleteTask(){
        service.deleteTaskById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    public TodoTask createTodoTask() {
        return new TodoTask(1L, "test", "test");
    }

    public TodoTaskDao createTodoTaskDao() {
        return new TodoTaskDao(1L, "test", "test");
    }

    public List<TodoTask> createTodoTaskList(TodoTask todoTask) {
        List<TodoTask> list = new ArrayList<>();
        list.add(todoTask);
        list.add(todoTask);
        return list;
    }

    public List<TodoTaskDao> createTodoTaskDaoList(TodoTaskDao todoTaskDao) {
        List<TodoTaskDao> list = new ArrayList<>();
        list.add(todoTaskDao);
        list.add(todoTaskDao);
        return list;
    }
}