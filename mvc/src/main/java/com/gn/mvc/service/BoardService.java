package com.gn.mvc.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gn.mvc.dto.AttachDto;
import com.gn.mvc.dto.BoardDto;
import com.gn.mvc.dto.PageDto;
import com.gn.mvc.dto.SearchDto;
import com.gn.mvc.entity.Attach;
import com.gn.mvc.entity.Board;
import com.gn.mvc.repository.AttachRepository;
import com.gn.mvc.repository.BoardRepository;
import com.gn.mvc.specification.BoardSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	private final BoardRepository repository;
	private final AttachRepository attachRepository;
	private final AttachService attachService;
	
		//삭제
		public int deleteBoard(Long id) {
			int result = 0;
			try {
				Board target = repository.findById(id).orElse(null);
				if(target != null) {
					 repository.deleteById(id);
				}
				result = 1;
			}catch(Exception e){
				e.printStackTrace();
			}
			
			return result;
		}
	
	
	
		//수정
		@Transactional(rollbackFor = Exception.class)
		public Board updateBoard(BoardDto param,List<AttachDto> attachUpdateDto) {
//			Board board = param.toEntity();
//			
//			Board result = repository.save(board);
//			
//			return new BoardDto().toDto(result);
			Board result = null;
			try {
				// 1. @Id를 쓴 필드를 기준으로 타겟 조회
				Board target = repository.findById(param.getBoard_no()).orElse(null);
				// 2. 타켓이 존재하는 경우 업데이트
				// target은 boardNo
				if (target != null) {
					// 3. 파일이 존재하는 경우(=삭제하고자 하는)
					if (param.getDelete_files() != null && !param.getDelete_files().isEmpty()) {
						for (Long attach_no : param.getDelete_files()) {
							// (1)메모리에서 파일 자체 삭제 
							if (attachService.deleteFileData(attach_no) > 0) {
								// (2)db에서 메타 데이터 삭제
								attachService.deleteMetaData(attach_no);
							}							
						}
					}
					if(attachUpdateDto.size() != 0) {
						for (AttachDto attachDto : attachUpdateDto) {
							attachDto.setBoard_no(param.getBoard_no());
							Attach attach = attachDto.toEntity();
							attachRepository.save(attach);
						}
					}
					
					
					result = repository.save(param.toEntity());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
	
	
//		@Autowired
//		BoardRepository repository;
	

		
		//단일조회
		public Board selectBoardOne(Long id) {
			return repository.findById(id).orElse(null);
		}
		
		@Transactional(rollbackFor = Exception.class)
		public int createBoard(BoardDto dto, List<AttachDto> attachList) {
			int result = 0;
			try {
				// 1. Board 엔티티 insert
				Board entity = dto.toEntity();
				Board saved = repository.save(entity);
				// 2. insert 결과로 반환받은 PK
				Long boardNo = saved.getBoardNo();
				// 3. attachList 데이터가 있는 겨우
				if (attachList.size() != 0) {
					for (AttachDto attachDto : attachList) {
						attachDto.setBoard_no(boardNo);
						Attach attach = attachDto.toEntity();
						// 4. Attach 엔티티 insert
						attachRepository.save(attach);
					}
				}

				result = 1;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
			
			
			
			//1. 매개변수 dto -> entity로 변경		
//			Board param = dto.toEntity();
//			
//			//2. repository의 save() 메소드 호출
//			Board result= repository.save(param);
//			
//			//3. 결과 entity -> dto
//			return new BoardDto().toDto(result);
//			
		}
		
		//목록조회
		public Page<Board>SelectBoardAll(SearchDto searchDto, PageDto pageDto){
			
			//pageable을 쓰면 sort을 쓰면 안댐
//			Sort sort = Sort.by("regDate").descending();
//			if(searchDto.getOrder_type() == 2) {
//				sort = Sort.by("regDate").ascending();
//			}
			
			//시작하는 데이터 번호, 한 페이지에 몇개씩,정렬
			//jpa에서는 페이지 1 이 0으로 인식 그래서 -1 으로 해줘야한다
			Pageable pageable = PageRequest.of(pageDto.getNowPage()-1, pageDto.getNumPerPage(), Sort.by("regDate").descending());
			if(searchDto.getOrder_type()==2) {
				pageable = PageRequest.of(pageDto.getNowPage()-1, pageDto.getNumPerPage(), Sort.by("regDate").ascending());
			}			
			//검색 창
			Specification<Board> spec = (root,query,criteriaBuilder) -> null;
			
			if(searchDto.getSearch_type() == 1) {
				spec = spec.and(BoardSpecification.boardTitleContains(searchDto.getSearch_text()));
			}else if(searchDto.getSearch_type() == 2) {
				spec = spec.and(BoardSpecification.boardTitleContains(searchDto.getSearch_text()));
			}else if(searchDto.getSearch_type() == 3) {
				spec = spec.and(BoardSpecification.boardTitleContains(searchDto.getSearch_text()))
					.or(BoardSpecification.boardTitleContains(searchDto.getSearch_text()));
			}
			//findAll에 sort도 같이 넘겨주자
			Page<Board>list = repository.findAll(spec,pageable);
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
