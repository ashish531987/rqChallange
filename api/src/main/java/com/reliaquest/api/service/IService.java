package com.reliaquest.api.service;

import java.util.List;
import java.util.Optional;

public interface IService<Entity, Input> {
    List<Entity> getAll();
    Optional<Entity> findById(Input id);
    List<Entity> findAllByNameContaining(String name);
    Optional<Entity> findHighestSalaryOfEmployee();
    List<Entity> findTopNEmployeesWithHighestSalary(int n);
    Entity saveAndFlush(Entity employee);
    Input delete(Input id);
}
