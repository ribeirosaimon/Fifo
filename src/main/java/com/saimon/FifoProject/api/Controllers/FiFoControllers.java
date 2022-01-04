package com.saimon.FifoProject.api.Controllers;

import com.saimon.FifoProject.Service.TaskService;
import com.saimon.FifoProject.api.Dto.MyTaskDTO;
import com.saimon.FifoProject.api.Exceptions.ApiErrors;
import com.saimon.FifoProject.api.Model.Entity.MyTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/task")
public class FiFoControllers {

    private final TaskService taskService;

    public FiFoControllers(TaskService taskService) {
        this.taskService = taskService;
    }


    @GetMapping("/{id}")
    public MyTaskDTO getTask(@PathVariable Long id) {
        MyTask foundTask = taskService.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new MyTaskDTO(foundTask.getId(), foundTask.getTaskDescription(), foundTask.getTimestamp());
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public MyTaskDTO createTask(@RequestBody @Valid MyTaskDTO task) {
        taskService.save(new MyTask(task.getDescription(), task.getTimestamp()));
        return task;
    }

    @PutMapping("/{id}")
    public MyTaskDTO update(@PathVariable Long id, @RequestBody MyTaskDTO dto) throws IllegalAccessException {
        MyTask foundTask = taskService.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        foundTask.setTaskDescription(dto.getDescription());
        foundTask.setTimestamp(dto.getTimestamp());
        taskService.update(foundTask);
        return new MyTaskDTO(foundTask.getId(), foundTask.getTaskDescription(), foundTask.getTimestamp());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws IllegalAccessException {
        MyTask foundTask = taskService.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        taskService.delete(foundTask);
    }

    @GetMapping
    public Page<MyTaskDTO> find(MyTaskDTO dto, Pageable pageRequest) {
        MyTask task = new MyTask(dto.getDescription(), dto.getTimestamp());
        Page<MyTask> result = taskService.find(task, pageRequest);
        List<MyTaskDTO> list = result
                .getContent()
                .stream()
                .map(entity -> new MyTaskDTO(entity.getId(),
                        entity.getTaskDescription(),
                        entity.getTimestamp()))
                .collect(Collectors.toList());
        return new PageImpl<MyTaskDTO>(list, pageRequest, result.getTotalElements());

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleValidationExeptions(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        return new ApiErrors(bindingResult);
    }


}
