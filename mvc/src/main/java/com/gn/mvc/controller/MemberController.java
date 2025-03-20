package com.gn.mvc.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gn.mvc.dto.MemberDto;
import com.gn.mvc.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {
	private final MemberService service;
	
	
	@GetMapping("/login")
	public String loginView(@RequestParam(value="error", required=false)String error,
			@RequestParam(value="errorMsg", required=false) String errorMsg,
			Model model) {
		//이거를 들고 login.html간다!
		model.addAttribute("error", error);		
		model.addAttribute("errorMsg", errorMsg);
		return "member/login";
	}
	
	@GetMapping("/signup")
	public String createMemberView() {
		return "member/create";
	}
	
@PostMapping("/signup")
@ResponseBody
public Map<String,String> createMemberApi(MemberDto dto){
	Map<String,String> resultMap = new HashMap<String,String>();
	resultMap.put("res_code","500");
	resultMap.put("res_msg","회원가입 중 오류가 발생하였습니다");
	
//	System.out.println(dto);
	
	MemberDto save = service.createMember(dto);
	
	if(save != null) {
		resultMap.put("res_code","200");
		resultMap.put("res_msg","회원가입 성공");
	}
	
	//service한테 insert 해주세요 라고 부탁
	
	
	return resultMap;
	
}

}
