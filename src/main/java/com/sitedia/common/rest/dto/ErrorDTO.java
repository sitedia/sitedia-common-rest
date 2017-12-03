package com.sitedia.common.rest.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Error DTO to be used by the level 1 error controller
 * @author sitedia
 *
 */
@Getter
@AllArgsConstructor
public class ErrorDTO {

    @ApiModelProperty(notes = "Code")
    private String error;

    @ApiModelProperty(notes = "Description")
    private String description;

    @ApiModelProperty(notes = "Errors")
    private List<String> errors;

    public ErrorDTO(String error, String description) {
        super();
        this.error = error;
        this.description = description;
    }

}
