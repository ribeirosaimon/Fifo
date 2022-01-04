package com.saimon.FifoProject.Service;

import com.saimon.FifoProject.api.Model.Entity.MyTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TaskService {

    MyTask save(MyTask book);

    Optional<MyTask> getById(Long id);

    void delete(MyTask foundTask) throws IllegalAccessException;

    MyTask update(MyTask foundTask) throws IllegalAccessException;

    Page<MyTask> find(MyTask task, Pageable pageRequest);
}
