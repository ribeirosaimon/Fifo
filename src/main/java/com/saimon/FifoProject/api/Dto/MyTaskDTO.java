package com.saimon.FifoProject.api.Dto;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class MyTaskDTO {
    private Long id;
    @NotNull
    private String description;
    private Long timestamp;

    public MyTaskDTO(Long id, String description, Long timestamp) {
        this.id = id;
        this.description = description;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        if (!(o instanceof MyTaskDTO)) return false;
        MyTaskDTO myTaskDTO = (MyTaskDTO) o;
        return getId() == myTaskDTO.getId() && getTimestamp() == myTaskDTO.getTimestamp() && Objects.equals(getDescription(), myTaskDTO.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDescription(), getTimestamp());
    }
}
