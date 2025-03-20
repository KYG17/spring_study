package com.gn.mvc.service;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
	private final DataSource dataSource;
	private final UserDetailsService userDetailsService;
	
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
	
	//항상 생각해야 하는 것
	//1. 반환형
	//2. 메소드명
	//3. 매개변수
	//4. 역할 : PK 값 기준 회원 정보 단일 조회 후 반환
	
	//수정을 위한 멤버 조회 메소드
	public Member selectMemberOne(Long id) {
		return repository.findById(id).orElse(null);
	}
	
	//회원 정보 수정 메소드 
	//역할
	public int updateMember(MemberDto param) {
		int result = 0;
		try {
			// (1)데이터베이스에 있는 회원 정보 수정
			// 패스워드 암화를 세팅해준다 그리고 repository.save
			param.setMember_pw(passwordEncoder.encode(param.getMember_pw()));
			Member updated = repository.save(param.toEntity());
			if (updated != null) {
				// (2)remember-me(**DB** <---, cookie)가 있다면 무효화
				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
				String sql = "DELETE FROM persistent_logins WHERE username = ?";
				jdbcTemplate.update(sql, param.getMember_id());
				// (3)변경된 회원 정보 Security Context에 즉시 반영
				UserDetails updateUserDetails = userDetailsService.loadUserByUsername(param.getMember_id());
				Authentication newAuth = new UsernamePasswordAuthenticationToken(updateUserDetails,
						updateUserDetails.getPassword(), updateUserDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(newAuth);
				result = 1;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	//삭제
	public int deleteMember(Long id) {
		int result = 0;
		try {
			Member deleteTarget = repository.findById(id).orElse(null);
			if(deleteTarget != null) {
				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
				String sql = "DELETE FROM persistent_logins WHERE username = ?";
				jdbcTemplate.update(sql, deleteTarget.getMemberId());
				SecurityContextHolder.getContext().setAuthentication(null);
				repository.deleteById(id);
			}	
			result = 1;
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
}
