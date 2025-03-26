package com.gn.todo.dto;








import com.gn.todo.entity.ToDo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ToDoDto {
	
	private Long no;
	private String new_ToDo;
	private String flag;
	
	//html에 hidden으로 flag을 사용하지 않고
	//DTO에 private String flag = "N"; 으로 추가
	
	public ToDo toEntity() {
		return ToDo.builder()
				.no(no)
				.content(new_ToDo)
				.flag(flag)
				.build();
	}
	
	public ToDoDto toDto(ToDo todo) {
		return ToDoDto.builder()
				.no(todo.getNo())
				.new_ToDo(todo.getContent())
				.flag(todo.getFlag())
				.build();
	}

}
