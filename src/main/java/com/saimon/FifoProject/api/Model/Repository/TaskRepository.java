package com.saimon.FifoProject.api.Model.Repository;

import com.saimon.FifoProject.api.Model.Entity.MyTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<MyTask, Long> {
}
