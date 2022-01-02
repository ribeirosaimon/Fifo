package com.saimon.FifoProject.Service;

import com.saimon.FifoProject.api.Model.Entity.MyTask;

import java.util.Optional;

public interface TaskService {

    MyTask save(MyTask book);
    Optional<MyTask> getById(Long id);
    void delete(MyTask foundTask);

    MyTask update(MyTask foundTask);
}
