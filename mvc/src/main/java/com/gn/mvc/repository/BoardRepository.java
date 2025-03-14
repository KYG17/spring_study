package com.gn.mvc.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gn.mvc.entity.Board;
//두번째 매개변수는 board클래스의 설정해둔 PK의 자료형
public interface BoardRepository extends JpaRepository<Board,Long>,JpaSpecificationExecutor<Board> {
	
	
	//3.Specification 사용
	List<Board> findAll(Specification<Board> spec);
	
	
	
	//1.메소드 네이밍!
	//제목을 기준
	List<Board> findByBoardTitleContaining(String keyword);
	//내용을 기준
	List<Board> findByBoardContentContaining(String keyword);	
	//제목 또는 내용
	List<Board> findByBoardTitleOrBoardContentContaining(String contentKeyword, String titleKeyword);
	
	//2.JPQL 이용
	@Query(value="SELECT b FROM Board b WHERE b.boardTitle LIKE CONCAT('%',?1 ,'%')")
	List<Board> findByTitleLike(String keyword);
	
	@Query(value="SELECT b FROM Board b WHERE b.boardContent LIKE CONCAT('%',?1,'%')")
	List<Board> findByContentLike(String keyword);
	
	@Query(value="SELECT b FROM Board b WHERE b.boardTitle LIKE CONCAT('%',?1,'%') OR b.boardContent LIKE CONCAT('%',?2,'%')")
	List<Board> findByTitleOrContentLike(String title,String content);
	
}
