package com.example.demo.service.core;

import com.example.demo.dto.users.core.UserOutputDto;

public interface EntityConvertor<T,V,E> {
    V convertInputToEntity(T t);
    E convertEntityToOutputDto(V v);
}
