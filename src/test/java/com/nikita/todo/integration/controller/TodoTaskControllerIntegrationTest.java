package com.nikita.todo.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikita.todo.business.repository.TodoTaskRepository;
import com.nikita.todo.business.repository.model.TodoTaskDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TodoTaskControllerIntegrationTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoTaskRepository mockRepository;


    @Before
    public void init() {
        TodoTaskDao dao = new TodoTaskDao(1L, "task 1", "yoga");
        when(mockRepository.findById(1L)).thenReturn(Optional.of(dao));
    }

    @Test
    public void getTaskById_OK() throws Exception {
        mockMvc.perform(get("/api/v1/todos/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("task 1")))
                .andExpect(jsonPath("$.description",is ("yoga")));
        verify(mockRepository,times(1)).findById(1L);
    }

    @Test
    public void findTaskByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/todos/10")).andExpect(status().isNotFound());
    }

    @Test
    public void getAllTasks() throws Exception {
        List<TodoTaskDao> todoTaskDaos = Arrays.asList(
                new TodoTaskDao(1L, "task 5", "hey"),
                new TodoTaskDao(2L, "task 6", "hi"));

        when(mockRepository.findAll()).thenReturn(todoTaskDaos);

        mockMvc.perform(get("/api/v1/todos/"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("task 5")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("task 6")));

        verify(mockRepository, times(1)).findAll();
    }
}