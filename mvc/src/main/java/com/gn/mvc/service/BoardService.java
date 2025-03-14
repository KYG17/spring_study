package com.gn.mvc.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gn.mvc.dto.BoardDto;
import com.gn.mvc.dto.SearchDto;
import com.gn.mvc.entity.Board;
import com.gn.mvc.repository.BoardRepository;
import com.gn.mvc.specification.BoardSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
//		@Autowired
//		BoardRepository repository;
	
		private final BoardRepository repository;
	
		public BoardDto createBoard(BoardDto dto) {
			//1. 매개변수 dto -> entity로 변경		
			Board param = dto.toEntity();
			
			//2. repository의 save() 메소드 호출
			Board result= repository.save(param);
			
			//3. 결과 entity -> dto
			return new BoardDto().toDto(result);
			
		}
		
		//목록조회
		public List<Board>SelectBoardAll(SearchDto searchDto){
			Specification<Board> spec = (root,query,criteriaBuilder) -> null;
			if(searchDto.getSearch_type() == 1) {
				spec = spec.and(BoardSpecification.boardTitleContains(searchDto.getSearch_text()));
			}else if(searchDto.getSearch_type() == 2) {
				spec = spec.and(BoardSpecification.boardTitleContains(searchDto.getSearch_text()));
			}else if(searchDto.getSearch_type() == 3) {
				spec = spec.and(BoardSpecification.boardTitleContains(searchDto.getSearch_text()))
					.or(BoardSpecification.boardTitleContains(searchDto.getSearch_text()));
			}
			List<Board>list = repository.findAll(spec);
			return list;

	
			//1~2번 사용한 경우
//			List<Board>list = new ArrayList<Board>();
			//else 절은 repository가 품고있는 메소드
			//Board repository에서 만든 메소드
//			if(searchDto.getSearch_type() == 1) {
//				//제목을 기준으로 검색
//				list = repository.findByTitleLike(searchDto.getSearch_text());
//			}else if(searchDto.getSearch_type() == 2) {
//				//내용을 기준으로 검색
//				list = repository.findByContentLike(searchDto.getSearch_text());
//			}else if(searchDto.getSearch_type() == 3) {
//				//제목+내용 기준으로 검색
//				list = repository.findByTitleOrContentLike(searchDto.getSearch_text(),searchDto.getSearch_text());
//			}else {
//				//Where 절 없이 검색
//				list = repository.findAll();
//			}
//			return list;
		}
}
