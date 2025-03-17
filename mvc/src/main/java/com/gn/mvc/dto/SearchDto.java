package com.gn.mvc.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SearchDto {
	//검색창 form 태그 안에 있는 name값들 -> select창과 검색창
	private int search_type;
	private String search_text;
	//정렬 창
	private int order_type;
}
