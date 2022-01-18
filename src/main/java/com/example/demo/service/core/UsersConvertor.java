package com.example.demo.service.core;

import com.example.demo.dto.users.core.UserOutputDto;

public interface UsersConvertor<T,V> {
    UserOutputDto convertEntityToOutputDto(T t);
    T convertInputToEntity(V v);


}
