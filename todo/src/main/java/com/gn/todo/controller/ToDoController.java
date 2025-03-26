package com.gn.todo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gn.todo.dto.PageDto;
import com.gn.todo.dto.SearchDto;
import com.gn.todo.dto.ToDoDto;
import com.gn.todo.entity.ToDo;
import com.gn.todo.service.ToDoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ToDoController {
	
	
	private final ToDoService service;

	
	//할 일 등록하기
	@PostMapping("/todo")
	@ResponseBody
	public Map<String,String> createNewToDo(ToDoDto todo , Model model){
		Map<String,String> result = new HashMap<String,String>();
		result.put("res_code", "500");
		result.put("res_msg", "할 일 추가 등록을 실패했습니다");
		
		ToDoDto newDto = service.createNewToDo(todo);
		
		
		//System.out.println(todo);
		
		//System.out.println(newDto);
		
		if(newDto != null) {
			result.put("res_code", "200");
			result.put("res_msg", "할 일 추가 등록 성공!!");
		}
				
		return result;
	}
	
	//목록 조회, 검색 , 페이징
	@GetMapping("/")
	public String selectlistAll(Model model, SearchDto searchDto, PageDto pageDto) {
		
		
	
		if(pageDto.getNowPage() == 0) pageDto.setNowPage(1);
		
		Page <ToDo> result = service.selectlistAll(searchDto,pageDto);
		
		//페이징 처리를 위해
//		if(result.isEmpty()) {
//			result = null;
//		}
	
		
		pageDto.setTotalPage(result.getTotalPages());
		
//		System.out.println(searchDto);
//		System.out.println(result);
		
		//page처리를 위해 ! 데이터가 없을 시에 조건문이 성립이 안됨 그래서 result.getContent로 보냄
		//그럼 home.html에 th:if="${#lists.isEmpty(todolist)} " 사용할 수 있음
		// => 페이징 , 그리고 list 에 조건문이 성립이 된다
		//model.addAttribute("todolist", result.getContent());
		
		model.addAttribute("todolist", result.getContent());
		model.addAttribute("searchDto",searchDto);
		model.addAttribute("pageDto",pageDto);
		
		return "home";
	}
	
	//삭제
	@DeleteMapping("/todo/{id}")
	@ResponseBody
	public Map<String,String> deleteToDo(@PathVariable("id") Long id){
//		System.out.println("삭제할 아이디!!!!!!!!!!!!!!!!!!" + id);
		Map<String,String> result = new HashMap<String,String>();
		result.put("res_code", "500");
		result.put("res_msg","삭제 실패!");
		
		int delete = service.deleteToDo(id);
		
		if(delete > 0) {
			result.put("res_code", "200");
			result.put("res_msg","삭제 성공!");
		}
		
		return result;
	}
	
	@PostMapping("/todo/{id}")
	@ResponseBody
	public Map<String,String> updateFlag(@PathVariable("id") Long id){
		Map<String,String> result = new HashMap<String,String>();
		result.put("res_code", "500");
		result.put("res_msg", "실패");
		
		//System.out.println("flag변경!!!!!!!!!!!" + id);
		
		ToDoDto updateDto  = service.updateFlag(id);
		
		if(updateDto != null) {
			result.put("res_code", "200");
			result.put("res_msg", "성공");
		}
//		
		return result;
	}
	
	
	
}
