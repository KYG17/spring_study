package com.gn.mvc.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gn.mvc.dto.MemberDto;
import com.gn.mvc.entity.Member;
import com.gn.mvc.service.MemberService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {
	private final MemberService service;
	private Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	@GetMapping("/login")
	public String loginView(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "errorMsg", required = false) String errorMsg, Model model) {
		// 이거를 들고 login.html간다!
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
	public Map<String, String> createMemberApi(MemberDto dto) {
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "회원가입 중 오류가 발생하였습니다");

//	System.out.println(dto);

		MemberDto save = service.createMember(dto);

		if (save != null) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "회원가입 성공");
		}

		// service한테 insert 해주세요 라고 부탁

		return resultMap;

	}

//화면전환
	@GetMapping("/member/{id}/update")
	public String memberUpdateView(@PathVariable("id") Long id, Model model) {
		//id를 보내줄게 조회해줘
//		logger.info(id.toString());
		Member member = service.selectMemberOne(id);
//		logger.info(member.toString());
		//model에 정보를 담고
		model.addAttribute("member",member);
		//member 아래 update로 보내준다 model를
		return "member/update";
	}
	
//회원정보수정
	@PostMapping("/member/{id}/update")
	@ResponseBody
	public Map<String,String> memberUpdateApi(MemberDto param, HttpServletResponse response){
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "회원 수정중 오류가 발생하였습니다.");
		
		//int로 반환
		int result = service.updateMember(param);
		if(result > 0) {
			//쿠키(remember-me) 무효화
			Cookie rememberMe = new Cookie("remember-me",null);
			rememberMe.setMaxAge(0);
			rememberMe.setPath("/");
			response.addCookie(rememberMe);
			
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "회원 수정 성공 ! 오예 !!" );
		}
		
		
		
		
		return resultMap;
	}
	
	//회원 탈퇴
	@DeleteMapping("/member/{id}")
	@ResponseBody
	public Map<String,String> deleteMemberApi(@PathVariable("id") Long id,HttpServletResponse response){
		logger.info(id.toString()+"회원삭제!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "회원 탈퇴 실패");
		
		Cookie rememberMe = new Cookie("remember-me",null);
		rememberMe.setMaxAge(0);
		rememberMe.setPath("/");
		response.addCookie(rememberMe);
		
		int result = service.deleteMember(id);
		if(result > 0) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "회원탈퇴 성공 그동안 감사했습니다");
		}
		
		return resultMap;
	}

}
