package com.gn.todo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gn.todo.dto.PageDto;
import com.gn.todo.dto.SearchDto;
import com.gn.todo.dto.ToDoDto;
import com.gn.todo.entity.ToDo;
import com.gn.todo.repository.ToDoRepository;
import com.gn.todo.specification.ToDoSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ToDoService {
	
	private final ToDoRepository repository;
	
	//기능 : 할 일 추가
	//반환형 : ToDoDto
	//매개변수 : dto
	public ToDoDto createNewToDo(ToDoDto dto) {
		
		ToDo param = dto.toEntity();
		
		ToDo result = repository.save(param);
		
		return new ToDoDto().toDto(result);
	}
	
	//기능 : 목록조회 , 페이징 , 검색
	//반환형 : Page 리스트
	//매개변수 : searchDto, pageDto
	public Page<ToDo> selectlistAll(SearchDto searchDto, PageDto pageDto){
		
		
		Pageable pageable = PageRequest.of(pageDto.getNowPage()-1,pageDto.getNumPerPage());
		
		//검색 창
		Specification<ToDo> spec =(root,query,criteriaBuilder) -> null;
		//이 조건문은  Controller에서도 쓸 수 있다.
		if (searchDto.getSearch_text() != null && !searchDto.getSearch_text().isEmpty()) {
		    spec = spec.and(ToDoSpecification.todoContentContains(searchDto.getSearch_text()));
		}
	
		Page <ToDo> list = repository.findAll(spec,pageable);
//		System.out.println(list);
		return list;
	}
	
	//기능 : 할 일 삭제
	//반환형 : int
	//매개변수 : id(PK값)
	public int deleteToDo(Long id) {
		int result = 0;
		try {
			ToDo target = repository.findById(id).orElse(null);
			if(target != null) {
				repository.deleteById(id);
			}
			result = 1;
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	
	public ToDoDto updateFlag(Long id) {
		ToDo target = repository.findById(id).orElse(null);
		//target을 dto을 바꾸기
//		ToDoDto dto = ToDoDto.builder()
//					.no(target.getNo())
//					.new_ToDo(target.getContent())
//					.flag(target.getFlag())
//					.build();
		
//		if(target != null){
//		if("Y".equals(target.getFlag())) dto.setFlag("N");
//		else dto.setFlag("Y")
//		}
//		return todoRepository.save(dto.toEntity());
		
		if(target != null) {
			if("Y".equals(target.getFlag())) {
				target.setFlag("N");				
			}else if("N".equals(target.getFlag())) {
				target.setFlag("Y");
			}
			repository.save(target);
			return new ToDoDto().toDto(target);
		}
		return null;
	}
	

}
