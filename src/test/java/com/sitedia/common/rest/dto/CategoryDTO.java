package com.sitedia.common.rest.dto;

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
public class CategoryDTO {

    @ApiModelProperty(notes = "Identifier")
    private Long id;

    @ApiModelProperty(notes = "Name")
    private String name;

}
