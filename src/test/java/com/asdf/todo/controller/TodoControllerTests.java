package com.asdf.todo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.asdf.todo.model.Todo;
import com.asdf.todo.service.TodoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;


@ExtendWith(SpringExtension.class)
@WebMvcTest(TodoController.class)
public class TodoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TodoService todoService;

    @Test
    public void testGetTodoById() throws Exception{
        Todo todo = new Todo();
        todo.setId(1L);
        todo.setTitle("Test Todo");

        given(todoService.findById(1L)).willReturn(todo);

        mockMvc.perform(get("/api/todos/v1/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Todo"));
    }

    @Test
    public void testGetAllTodos() throws Exception{
        given(todoService.findAll()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/api/todos/v1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        given(todoService.findAll())
                .willReturn(
                        Collections.singletonList(
                                new Todo(1L, "Test Todo", "Description", false)));

        mockMvc.perform(get("/api/todos/v1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Test Todo"));

    }

    @Test
    public void testCreateTodo() throws Exception{
        Todo todo = new Todo();
        todo.setId(1L);
        todo.setTitle("New Todo");

        given(todoService.save(any(Todo.class))).willReturn(todo);

        mockMvc.perform(
                post("/api/todos/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        // 교재에서는 주석대로 작성해야하는데 그럴 경우
                        // 테스트를 통과하지 못함
                        // 제미나이에게 물어본 결과 주석 아래의 값대로 입력해야
                        // 테스트를 정상적으로 통과함
                        // .content("{\"title\": \"New Todo\"}"))
                        .content("{\"title\": \"New Todo\", \"description\": \"example\", \"completed\": false}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("New Todo"));

    }

    @Test
    public void testUpdateTodo() throws Exception{
        Todo existingTodo = new Todo();
        existingTodo.setId(1L);
        existingTodo.setTitle("Existing Todo");

        Todo updatedTodo = new Todo();
        updatedTodo.setId(1L);
        updatedTodo.setTitle("Updated Todo");

        given(todoService.findById(1L)).willReturn(existingTodo);
        given(todoService.update(anyLong(), any(Todo.class)))
                .willReturn(updatedTodo);

        mockMvc.perform(
                put("/api/todos/v1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        // 교재에서는 주석대로 작성해야하는데 그럴 경우
                        // 테스트를 통과하지 못함
                        // 제미나이에게 물어본 결과 주석 아래의 값대로 입력해야
                        // 테스트를 정상적으로 통과함
                        // .content("{\"title\": \"Updated Todo\"}"))
                        .content("{\"title\": \"Updated Todo\", \"description\": \"example\", \"completed\": true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Updated Todo"));
    }

    @Test
    public void testDeleteTodo() throws Exception{
        Todo todo = new Todo();
        todo.setId(1L);
        todo.setTitle("Test Todo");

        given(todoService.findById(1L)).willReturn(todo);

        mockMvc.perform(delete("/api/todos/v1/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}





















