package com.sitedia.common.rest.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Response list DTO to use when returning a list of results with the count
 * @author cedric
 *
 */
@Getter
@AllArgsConstructor
public class ResponseListDTO<T> {

    @ApiModelProperty(notes = "List")
    private List<T> list;

    @ApiModelProperty(notes = "Total number of elements, for pagination purpose")
    private Long count;

}
