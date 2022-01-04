package com.saimon.FifoProject.ModelTest;

import com.saimon.FifoProject.api.Model.Entity.MyTask;
import com.saimon.FifoProject.api.Model.Repository.TaskRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class TaskRepositoryTest {
    static long TEST_ID = 1L;
    static String TEST_DESCRIPTION = "task description";
    static long TEST_TIMESTAMP = 10L;
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    TaskRepository taskRepository;

    @Test
    @DisplayName("Get Task by Id")
    public void getTaskById() {
        MyTask task = new MyTask(TEST_DESCRIPTION, TEST_TIMESTAMP);
        entityManager.persist(task);
        Optional<MyTask> foundTask = taskRepository.findById(task.getId());

        Assertions.assertThat(foundTask.get().getId()).isNotNull();
        Assertions.assertThat(foundTask.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Save Task")
    public void saveTask() {
        MyTask task = new MyTask(TEST_DESCRIPTION, TEST_TIMESTAMP);
        MyTask savedTask = taskRepository.save(task);
        Assertions.assertThat(savedTask.getId()).isNotNull();
    }

    @Test
    @DisplayName("Delete Task")
    public void deleteTask() {
        MyTask task = new MyTask(TEST_DESCRIPTION, TEST_TIMESTAMP);
        entityManager.persist(task);
        MyTask foundTask = entityManager.find(MyTask.class, task.getId());
        taskRepository.delete(foundTask);
        MyTask deletedTask = entityManager.find(MyTask.class, task.getId());
        Assertions.assertThat(deletedTask).isNull();

    }
}
