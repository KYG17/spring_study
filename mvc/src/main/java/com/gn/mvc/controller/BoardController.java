package com.gn.mvc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gn.mvc.dto.BoardDto;
import com.gn.mvc.dto.SearchDto;
import com.gn.mvc.entity.Board;
import com.gn.mvc.service.BoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BoardController {
	//약속
	private Logger logger = LoggerFactory.getLogger(BoardController.class); 
	//BoardService를 사용할 것이다 라는 것을 선언
	//1.필드 주입 -> 순환 참조 
//	@Autowired
//	BoardService service;
	
	//2.메소드(Setter) 주입 방법 -> 불변성 보장 X
//	private BoardService service;
//	
//	@Autowired
//	public void setBoardService(BoardService service) {
//		this.service = service;
//	}
	
	//3.생성자 주입 방법 + final -> 이 방법을 쓴다!!!!!!!! @RequiredArgsConstructor 사용
	private final BoardService service;

	//	@Autowired --> @RequiredArgsConstructor 애를 사용하면 아래 메소드를 안 써도 된다..
//	public BoardController(BoardService service) {
//		this.service = service;
//	}
	
	//a 태그에 url요청받은것을 괄호안에
	@GetMapping("/board/create")
	public String createBoardView() {
		//template 밑에 board 밑에 create로 !
		return "board/create";
	}
	
	
	
	
	
	//fetch의 url
	//html에서 json으로 데이터를 받겠다고 함, 응답할 때 json 형태로 데이터를 html에 줘야함
	//그래서 메소드에 String을 사용하지 않고 MAP<String,String>을 사용 그리고 어노테인션 @ResponseBody 사용
	
	//@RequestParam 매개변수에 form안에 input의 name값 그리고 애네들을 어떻게 사용 할 것 인지
	@PostMapping("/board")
	@ResponseBody
	public Map<String,String> createBoardApi(
			
			
			//한땀한땀 작성하는법
			//@RequestParam("board_title") String boardTitle,
			//@RequestParam("board_content") String boardContent
			
			//Map으로 사용
			//@RequestParam Map<String,String> param
			
			//3가지 다 똑같은 표현!!
			BoardDto dto
			) {
			Map<String,String> resultMap = new HashMap<String,String>();
			
			
			resultMap.put("res_code", "500");
			resultMap.put("res_msg", "게시글 등록 중 오류가 발생하였습니다");
			System.out.println(dto);
			//Service가 가지고 있는 createBoard메소드 호출
			BoardDto result = service.createBoard(dto);
			
			logger.debug("1 : " + result.toString());
			logger.info("2 : " + result.toString());
			logger.warn("3 : " + result.toString());
			logger.error("4 : " + result.toString());
			return resultMap;
	}
	
	//목록조회
	@GetMapping("/board")
	public String SelectBoardAll(Model model,SearchDto searchDto ) {
		//1.DB에서 목록 select
		List<Board> resultlist = service.SelectBoardAll(searchDto);
		//2.목록 Model에 등록
		//model에 service에서 가져온 정보를 key value형식으로 전달
		//3.list.html에 데이터 셋팅
		model.addAttribute("boardList",resultlist); 
		model.addAttribute("searchDto",searchDto);
		
		
		
		return "board/list";
	}
	
	
	
	
	
	
	
	
	
}
