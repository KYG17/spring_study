package com.gn.mvc.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gn.mvc.entity.ChatMsg;
import com.gn.mvc.entity.ChatRoom;
import com.gn.mvc.repository.ChatMsgRepository;
import com.gn.mvc.repository.ChatRoomRepository;
import com.gn.mvc.specification.ChatMsgSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMsgService {
	private final ChatMsgRepository repository;
	private final ChatRoomRepository chatRoomRepository;
	// 1. 기능 : 채팅메시지 목록 조회
	// 2. 조건 : 채팅방 번호
	// 3. 결과 : 채팅 메시지 목록
	public List<ChatMsg> selectChatMsgAll(Long id){
		//조건 : 채팅방 번호
		//(1) : 전달받은 id를 기준으로 ChatRoom엔티티를 조회
		ChatRoom entity = chatRoomRepository.findById(id).orElse(null);
		System.out.println(entity);
		//(2) : ChatRoom 엔티티 기준으로 Specification 생성
		Specification<ChatMsg> spec = ChatMsgSpecification.chatMsgList(entity);
		System.out.println(spec);
		//(3) : spec을 매개변수로 전달하여 findAll 반환
		return repository.findAll(spec);
	}
}
