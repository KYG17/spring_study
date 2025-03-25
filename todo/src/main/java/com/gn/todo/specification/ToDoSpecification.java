package com.gn.todo.specification;

import org.springframework.data.jpa.domain.Specification;

import com.gn.todo.entity.ToDo;

public class ToDoSpecification {
	public static Specification<ToDo> todoContentContains(String keyword){
		return (root,query,criteriaBuilder) ->
			criteriaBuilder.like(root.get("content"),  "%"+keyword+"%");
	}
}
