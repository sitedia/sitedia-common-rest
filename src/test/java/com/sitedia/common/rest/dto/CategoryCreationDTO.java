package com.sitedia.common.rest.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO to create a user
 * 
 * @author cedric
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CategoryCreationDTO {

    @NotNull(message = "{error.name.required}")
    @Size(min = 2, max = 256, message = "{error.name.invalid}")
    @ApiModelProperty(notes = "Name")
    private String name;

}
