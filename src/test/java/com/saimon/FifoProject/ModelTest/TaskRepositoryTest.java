package com.saimon.FifoProject.ModelTest;

import com.saimon.FifoProject.api.Model.Repository.TaskRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class TaskRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    TaskRepository taskRepository;


}
