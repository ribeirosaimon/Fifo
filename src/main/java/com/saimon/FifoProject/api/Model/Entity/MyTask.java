package com.saimon.FifoProject.api.Model.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class MyTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String taskDescription;
    private Long timestamp;

    public MyTask(String taskDescription, Long timestamp) {
        this.taskDescription = taskDescription;
        this.timestamp = timestamp;
    }

    public MyTask() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MyTask)) return false;
        MyTask myTask = (MyTask) o;
        return Objects.equals(getId(), myTask.getId()) && Objects.equals(getTaskDescription(), myTask.getTaskDescription()) && Objects.equals(getTimestamp(), myTask.getTimestamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTaskDescription(), getTimestamp());
    }
}
