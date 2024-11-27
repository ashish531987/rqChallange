package com.reliaquest.api.model;

import lombok.Data;

@Data
public abstract class BaseResponse<T>{
    T data;
    String status;
}
