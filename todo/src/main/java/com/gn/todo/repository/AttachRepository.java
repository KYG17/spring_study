package com.gn.todo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gn.todo.entity.Attach;

public interface AttachRepository extends JpaRepository<Attach,Long> {
	
}
