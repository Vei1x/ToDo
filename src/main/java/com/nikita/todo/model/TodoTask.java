package com.nikita.todo.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodoTask {

    @ApiModelProperty(hidden = true)
    Long id;
    @NotNull
    String name;
    @NotNull
    String description;

}
