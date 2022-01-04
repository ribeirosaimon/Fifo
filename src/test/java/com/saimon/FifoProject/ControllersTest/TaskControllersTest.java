package com.saimon.FifoProject.ControllersTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saimon.FifoProject.Service.TaskService;
import com.saimon.FifoProject.api.Dto.MyTaskDTO;
import com.saimon.FifoProject.api.Model.Entity.MyTask;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class TaskControllersTest {
    static long TEST_ID = 1L;
    static String TEST_DESCRIPTION = "task description";
    static long TEST_TIMESTAMP = 1000L;

    static String TASK_API = "/api/task";

    @Autowired
    MockMvc mvc;
    @MockBean
    TaskService taskService;

    @Test
    @DisplayName("Add simple Task")
    public void addSimpleTask() throws Exception {
        String json = new ObjectMapper().writeValueAsString(new MyTaskDTO(TEST_ID, TEST_DESCRIPTION, TEST_TIMESTAMP));

        MyTask savedTask = new MyTask(TEST_DESCRIPTION, TEST_TIMESTAMP);
        savedTask.setId(TEST_ID);
        BDDMockito.given(taskService.save(Mockito.any(MyTask.class))).willReturn(savedTask);


        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(String.format(TASK_API + "/add"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(TEST_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("description").value(TEST_DESCRIPTION))
                .andExpect(MockMvcResultMatchers.jsonPath("timestamp").value(TEST_TIMESTAMP));

    }

    @Test
    @DisplayName("Error to create Task")
    public void createInvalidTaskTest() throws Exception {
        String json = new ObjectMapper().writeValueAsString(new MyTaskDTO(TEST_ID, null, TEST_TIMESTAMP));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(String.format(TASK_API.concat("/add")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(1)));

    }

    @Test
    @DisplayName("Task not found")
    public void getTaskNotFound() throws Exception {
        MyTask savedTask = new MyTask(TEST_DESCRIPTION, TEST_TIMESTAMP);
        savedTask.setId(TEST_ID);

        BDDMockito.given(taskService.getById(Mockito.anyLong())).willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(TASK_API.concat("/" + TEST_ID))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Get Description Task")
    public void getDescriptionTask() throws Exception {
        MyTask savedTask = new MyTask(TEST_DESCRIPTION, TEST_TIMESTAMP);
        savedTask.setId(TEST_ID);

        BDDMockito.given(taskService.getById(TEST_ID)).willReturn(Optional.of(savedTask));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(TASK_API.concat("/" + TEST_ID))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(TEST_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("description").value(TEST_DESCRIPTION))
                .andExpect(MockMvcResultMatchers.jsonPath("timestamp").value(TEST_TIMESTAMP));

    }

    @Test
    @DisplayName("Delete Task")
    public void deleteTask() throws Exception {
        MyTask savedTask = new MyTask(TEST_DESCRIPTION, TEST_TIMESTAMP);
        savedTask.setId(TEST_ID);
        BDDMockito.given(taskService.getById(Mockito.anyLong())).willReturn(Optional.of(savedTask));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(TASK_API.concat("/" + TEST_ID));

        mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("No found Task to delete")
    public void noFoundTask() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(TASK_API.concat("/" + TEST_ID));
        mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Update Task")
    public void updateTask() throws Exception {
        String ChangeDescription = "new description";
        String json = new ObjectMapper()
                .writeValueAsString(new MyTaskDTO(TEST_ID, ChangeDescription, TEST_TIMESTAMP));

        MyTask savedTask = new MyTask(TEST_DESCRIPTION, TEST_TIMESTAMP);
        savedTask.setId(TEST_ID);
        BDDMockito.given(taskService.getById(TEST_ID)).willReturn(Optional.of(savedTask));

        MyTask changeTask = new MyTask(TEST_DESCRIPTION, TEST_TIMESTAMP);
        changeTask.setId(TEST_ID);
        BDDMockito.given(taskService.update(savedTask)).willReturn(changeTask);


        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(TASK_API.concat("/" + TEST_ID))
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
        mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(TEST_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("description").value(ChangeDescription))
                .andExpect(MockMvcResultMatchers.jsonPath("timestamp").value(TEST_TIMESTAMP));
        ;
    }

    @Test
    @DisplayName("No found Task to update")
    public void noFoundTaskUpdated() throws Exception {
        String json = new ObjectMapper()
                .writeValueAsString(new MyTaskDTO(TEST_ID, TEST_DESCRIPTION, TEST_TIMESTAMP));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(TASK_API.concat("/" + TEST_ID))
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
        mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Filter Task")
    public void filterTask() throws Exception {
        MyTask savedTask = new MyTask(TEST_DESCRIPTION, TEST_TIMESTAMP);
        savedTask.setId(TEST_ID);

        BDDMockito.given(taskService.find(Mockito.any(MyTask.class), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<MyTask>(Arrays.asList(savedTask), PageRequest.of(0, 100), 1));

        String queryString = String.format("?taskDescription=%s&timestamp=%s&page=0&size=100",  savedTask.getTaskDescription(), savedTask.getTimestamp());
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(TASK_API.concat(queryString))
                .accept(MediaType.APPLICATION_JSON);
        mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("content", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("totalElements").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("pageable.pageSize").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("pageable.pageNumber").value(0))
        ;

    }
}
