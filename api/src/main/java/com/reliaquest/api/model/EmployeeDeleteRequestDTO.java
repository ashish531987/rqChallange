package com.reliaquest.api.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;


@AllArgsConstructor
@Data
public class EmployeeDeleteRequestDTO implements Serializable {
    @NotBlank
    private String name;
}
