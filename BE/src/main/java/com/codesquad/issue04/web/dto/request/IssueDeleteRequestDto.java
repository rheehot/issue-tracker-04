package com.codesquad.issue04.web.dto.request;

import lombok.Getter;

@Getter
public class IssueDeleteRequestDto {
	private Long id;

	public IssueDeleteRequestDto(Long id) {
		this.id = id;
	}
}
