package com.example.demo.controller.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class ResponseResult<T> {
    private Integer code;
    private T data;
    private List<T> dataList;
    private String message;
}
