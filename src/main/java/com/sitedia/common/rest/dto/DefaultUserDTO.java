package com.sitedia.common.rest.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DefaultUserDTO {

    private String username;

    private String password;

    private List<String> roles;

}
