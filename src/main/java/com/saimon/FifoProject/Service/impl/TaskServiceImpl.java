package com.saimon.FifoProject.Service.impl;

import com.saimon.FifoProject.Service.TaskService;
import com.saimon.FifoProject.api.Model.Entity.MyTask;
import com.saimon.FifoProject.api.Model.Repository.TaskRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public void delete(MyTask foundTask) throws IllegalAccessException {
        if (foundTask == null || foundTask.getId() == null){
            throw new IllegalAccessException("Task id can't be null.");
        }
        taskRepository.delete(foundTask);
    }

    @Override
    public MyTask update(MyTask foundTask) throws IllegalAccessException {
        if (foundTask == null || foundTask.getId() == null){
            throw new IllegalAccessException("Task id can't be null.");
        }
        return taskRepository.save(foundTask);
    }

    @Override
    public Page<MyTask> find(MyTask task, Pageable pageRequest) {
        Example<MyTask> example = Example.of(task,
                ExampleMatcher
                        .matching()
                        .withIgnoreCase()
                        .withIgnoreNullValues()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));
        return taskRepository.findAll(example, pageRequest);
    }
}
