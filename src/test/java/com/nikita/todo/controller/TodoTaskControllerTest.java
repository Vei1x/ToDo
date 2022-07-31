package com.nikita.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikita.todo.business.repository.model.TodoTaskDao;
import com.nikita.todo.business.service.impl.TodoTaskServiceImpl;
import com.nikita.todo.model.TodoTask;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoTaskController.class)
class TodoTaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoTaskServiceImpl service;


    private final String URL = "/api/v1/todo";


    @Test
    void getTaskById() throws Exception {
        TodoTask task = createTask(1L);

        when(service.findTaskById(task.getId()))
                .thenReturn(Optional.of(task));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/" + task.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isFound());

        verify(service, times(1))
                .findTaskById(task.getId());
    }

    @Test
    void getTaskById_NotFound() throws Exception {
        TodoTask task = createTask(1L);

        when(service.findTaskById(task.getId()))
                .thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/" + task.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service, times(1))
                .findTaskById(task.getId());
    }

    @Test
    void getAllTasks() throws Exception {
        when(service.findAllTasks())
                .thenReturn(new ArrayList<>(Arrays.asList(
                        new TodoTask(1L, "task 1", "Homework"),
                        new TodoTask(2L, "task 2", "yoga"))));

        mockMvc.perform(MockMvcRequestBuilders
                .get(URL))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(status().isFound());

        verify(service,times(1)).findAllTasks();
    }

    @Test
    void getAllTasks_EmptyList() throws Exception {
        List<TodoTask> list = new ArrayList<>();
        when(service.findAllTasks()).thenReturn(list);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL)
                        .content(asJsonString(list))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service, times(1)).findAllTasks();
    }



    @Test
    void addTask() throws Exception {
        TodoTask task = createTask(null);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(task))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(service, times(1))
                .addTask(task);

    }

    @Test
    void addTask_idInBody() throws Exception {
        TodoTask task = createTask(1L);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(task))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(service, times(0))
                .addTask(task);

    }

    @Test
    void editTaskById() throws Exception {
        TodoTask task = createTask(1L);

        when(service.findTaskById(task.getId())).thenReturn(Optional.of(task));

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/1")
                        .content(asJsonString(task))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(status().isOk());

        verify(service, times(1)).editTask(task);

    }

    @Test
    void editTaskById_NoBodyId() throws Exception {
        TodoTask task = createTask(null);

        when(service.findTaskById(task.getId())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/1")
                        .content(asJsonString(task))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(service, times(0)).editTask(task);

    }

    @Test
    void editTaskById_TaskNoFound() throws Exception {
        TodoTask task = createTask(2L);

        when(service.findTaskById(2L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/2")
                        .content(asJsonString(task))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service, times(0)).editTask(task);
    }

    @Test
    void deleteTask() throws Exception {
        TodoTask task = createTask(1L);
        when(service.findTaskById(task.getId())).thenReturn(Optional.of(task));
        mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(service, times(1)).deleteTaskById(task.getId());
    }

    @Test
    void deleteFeedbackInvalid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service, times(0)).deleteTaskById(1L);
    }


        public TodoTask createTask(Long id){
        return new TodoTask(id, "test","test");
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}