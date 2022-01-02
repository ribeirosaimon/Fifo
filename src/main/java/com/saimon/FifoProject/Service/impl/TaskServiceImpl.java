package com.saimon.FifoProject.Service.impl;

import com.saimon.FifoProject.Service.TaskService;
import com.saimon.FifoProject.api.Model.Entity.MyTask;
import com.saimon.FifoProject.api.Model.Repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public MyTask save(MyTask task) {
        return taskRepository.save(task);
    }

    @Override
    public Optional<MyTask> getById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public void delete(MyTask foundTask) {
        taskRepository.delete(foundTask);
    }

    @Override
    public MyTask update(MyTask foundTask) {
        return taskRepository.save(foundTask);
    }
}
