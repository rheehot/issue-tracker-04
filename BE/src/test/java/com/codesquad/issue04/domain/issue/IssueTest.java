package com.codesquad.issue04.domain.issue;

import static org.assertj.core.api.Assertions.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.codesquad.issue04.domain.label.Label;
import com.codesquad.issue04.domain.milestone.Milestone;
import com.codesquad.issue04.domain.user.User;
import com.codesquad.issue04.web.dto.response.IssueOverviewResponseDto;

@SpringBootTest
public class IssueTest {

	private User user;

	@BeforeEach
	void setUp() {
		this.user = User.builder()
		.id(1L)
		.githubId("guswns1659")
		.image("image")
		.name("Jack")
		.issues(Collections.emptyList())
		.build();
	}

	@DisplayName("Null인 필드의 빈 객체를 반환한다.")
	@Test
	void 이슈필드의_Null_테스트() {

		Issue issue = Issue.builder()
			.id(1L)
			.comments(null)
			.labels(Collections.emptySet())
			.milestone(new Milestone())
			.title("title")
			.user(this.user)
			.build();


		assertThat(issue.getComments()).isNotNull();

	}
}
