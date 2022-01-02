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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
    public void setUp(){
        this.taskService = new TaskServiceImpl(taskRepository);
    }

    @Test
    @DisplayName("Save a simple task")
    public void saveTask(){
        MyTask task = new MyTask(TEST_DESCRIPTION, TEST_TIMESTAMP);
        task.setId(TEST_ID);
        Mockito.when(taskRepository.save(task)).thenReturn(task);
        MyTask savedTask = taskService.save(task);

        Assertions.assertThat(savedTask.getId()).isNotNull();
        Assertions.assertThat(savedTask.getTaskDescription()).isEqualTo(TEST_DESCRIPTION);
        Assertions.assertThat(savedTask.getTimestamp()).isEqualTo(TEST_TIMESTAMP);

    }
}
