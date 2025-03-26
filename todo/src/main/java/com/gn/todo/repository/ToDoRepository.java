package com.gn.todo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gn.todo.entity.ToDo;

public interface ToDoRepository extends JpaRepository<ToDo,Long> 
							,JpaSpecificationExecutor<ToDo>{
	
	Page <ToDo> findAll(Specification<ToDo>spec,Pageable pageable);
	
	
}
