package com.gn.mvc.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gn.mvc.dto.MemberDto;
import com.gn.mvc.entity.Member;
import com.gn.mvc.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository repository; 
	private final PasswordEncoder passwordEncoder;
	
	public MemberDto createMember(MemberDto dto) {
		//비밀번호 암호화
		dto.setMember_pw(passwordEncoder.encode(dto.getMember_pw()));
		
		
		Member param = dto.toEntity();
		
		Member result = repository.save(param);
		
//		System.out.println(result);
//		
//		System.out.println(param);
		
		return new MemberDto().toDto(result);
	}
}
