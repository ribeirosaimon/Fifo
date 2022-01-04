package com.saimon.FifoProject.ServiceTest;

import com.saimon.FifoProject.Service.TaskService;
import com.saimon.FifoProject.Service.impl.TaskServiceImpl;
import com.saimon.FifoProject.api.Model.Entity.MyTask;
import com.saimon.FifoProject.api.Model.Repository.TaskRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class TaskServiceTest {
    static long TEST_ID = 1L;
    static String TEST_DESCRIPTION = "task description";
    static long TEST_TIMESTAMP = 10L;

    TaskService taskService;

    @MockBean
    TaskRepository taskRepository;

    @BeforeEach
    public void setUp() {
        this.taskService = new TaskServiceImpl(taskRepository);
    }

    @Test
    @DisplayName("Save a simple task")
    public void saveTask() {
        MyTask task = new MyTask(TEST_DESCRIPTION, TEST_TIMESTAMP);
        task.setId(TEST_ID);
        Mockito.when(taskRepository.save(task)).thenReturn(task);
        MyTask savedTask = taskService.save(task);

        Assertions.assertThat(savedTask.getId()).isNotNull();
        Assertions.assertThat(savedTask.getTaskDescription()).isEqualTo(TEST_DESCRIPTION);
        Assertions.assertThat(savedTask.getTimestamp()).isEqualTo(TEST_TIMESTAMP);
    }

    @Test
    @DisplayName("Get Task by id")
    public void getById() {
        MyTask task = new MyTask(TEST_DESCRIPTION, TEST_TIMESTAMP);
        task.setId(TEST_ID);
        Mockito.when(taskRepository.findById(TEST_ID)).thenReturn(Optional.of(task));

        Optional<MyTask> foundTask = taskService.getById(TEST_ID);

        Assertions.assertThat(foundTask.isPresent()).isTrue();
        Assertions.assertThat(foundTask.get().getId()).isEqualTo(TEST_ID);
        Assertions.assertThat(foundTask.get().getTaskDescription()).isEqualTo(TEST_DESCRIPTION);
        Assertions.assertThat(foundTask.get().getTimestamp()).isEqualTo(TEST_TIMESTAMP);
    }

    @Test
    @DisplayName("Empty Task with not id")
    public void taskNotFound() {
        Mockito.when(taskRepository.findById(TEST_ID)).thenReturn(Optional.empty());
        Optional<MyTask> foundTask = taskService.getById(TEST_ID);
        Assertions.assertThat(foundTask.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Delete Task")
    public void deleteTask() {
        MyTask task = new MyTask(TEST_DESCRIPTION, TEST_TIMESTAMP);
        task.setId(TEST_ID);
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> taskService.delete(task));
        Mockito.verify(taskRepository, Mockito.times(1)).delete(task);
    }

    @Test
    @DisplayName("Error not found Task to delete")
    public void errorDeleteTask() {
        MyTask task = new MyTask();
        org.junit.jupiter.api.Assertions.assertThrows(IllegalAccessException.class, () -> taskService.delete(task));
        Mockito.verify(taskRepository, Mockito.never()).delete(task);
    }

    @Test
    @DisplayName("Error not found Task to update")
    public void errorUpdateTask() {
        MyTask task = new MyTask();
        org.junit.jupiter.api.Assertions.assertThrows(IllegalAccessException.class, () -> taskService.update(task));
        Mockito.verify(taskRepository, Mockito.never()).save(task);
    }

    @Test
    @DisplayName("Update Task")
    public void updateTask() throws IllegalAccessException {
        String updatedDescription = "updated Task";
        MyTask updatedTask = new MyTask(updatedDescription, TEST_TIMESTAMP);
        updatedTask.setId(TEST_ID);

        MyTask task = new MyTask(TEST_DESCRIPTION, TEST_TIMESTAMP);
        task.setId(TEST_ID);

        Mockito.when(taskRepository.save(task)).thenReturn(task);

        MyTask testTask = taskService.update(updatedTask);
        Assertions.assertThat(updatedTask.getId()).isEqualTo(task.getId());
        Assertions.assertThat(updatedTask.getTaskDescription()).isEqualTo(updatedDescription);
        Assertions.assertThat(updatedTask.getTimestamp()).isEqualTo(TEST_TIMESTAMP);
    }

    @Test
    @DisplayName("FilterTask")
    public void filterTask() {
        MyTask task = new MyTask(TEST_DESCRIPTION, TEST_TIMESTAMP);
        task.setId(TEST_ID);

        List<MyTask> list = Arrays.asList(task);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<MyTask> page = new PageImpl<MyTask>(Arrays.asList(task), PageRequest.of(0, 10), 1);
        Mockito.when(taskRepository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class)))
                .thenReturn(page);
        Page<MyTask> result = taskService.find(task, pageRequest);
        Assertions.assertThat(result.getTotalElements()).isEqualTo(1);
        Assertions.assertThat(result.getContent()).isEqualTo(list);
        Assertions.assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        Assertions.assertThat(result.getPageable().getPageSize()).isEqualTo(10);

    }
}
