package com.example.demo.service.core;

import com.example.demo.Exceptions.EntityNotFoundException;
import com.example.demo.Exceptions.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class AbstractCRUD <T,ID> {

    private JpaRepository<T,ID> jpaRepository;

    public void setJpaRepository(JpaRepository<T, ID> jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Transactional
    public T save(T entity){
        return jpaRepository.save(entity);
    }

    @Transactional
    public T update(T entity){
        return jpaRepository.save(entity);
    }


    public T loadById(ID id){
        return  jpaRepository.findById(id).orElseThrow(
                ()-> new NotFoundException("Entity with id " + id + " not existed!")
        );
    }

    public List<T> loadAll(){
        return jpaRepository.findAll();
    }

    @Transactional
    public void deleteById(ID id){
        jpaRepository.deleteById(id);
    }


}
