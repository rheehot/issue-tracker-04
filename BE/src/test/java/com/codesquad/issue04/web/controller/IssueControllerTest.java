package com.codesquad.issue04.web.controller;

import static org.assertj.core.api.Assertions.*;

import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.codesquad.issue04.web.dto.response.error.ErrorResponseDto;
import com.codesquad.issue04.web.dto.response.issue.IssueOverviewResponseDtos;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "1500")
public class IssueControllerTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void 전체_요약_이슈를_요청해서_응답한다() {

		// given
		String url = "http://localhost:" + port + "/api/v1/issues";

		// when
		ResponseEntity<IssueOverviewResponseDtos> responseEntity
			= restTemplate.getForEntity(url, IssueOverviewResponseDtos.class);

		// then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isInstanceOf(IssueOverviewResponseDtos.class);
	}

	@Test
	void 전체_열린_이슈만_선별해서_응답한다() {
		String url = "http://localhost:" + port + "/api/v1/issues?status=open";
		webTestClient.get()
			.uri(url)
			.exchange()
			.expectStatus().isEqualTo(200)
			.expectBodyList(IssueOverviewResponseDtos.class)
			.consumeWith(response ->
				assertThat(
					Objects.requireNonNull(response.getResponseBody()).stream()
						.allMatch(allData -> allData.getAllData().stream()
							.allMatch(issue -> issue.getStatus().isOpen()))
				).isEqualTo(true));
	}

	@Test
	void 전체_닫힌_이슈만_선별해서_응답한다() {
		String url = "http://localhost:" + port + "/api/v1/issues?status=closed";
		webTestClient.get()
			.uri(url)
			.exchange()
			.expectStatus().isEqualTo(200)
			.expectBodyList(IssueOverviewResponseDtos.class)
			.consumeWith(response ->
				assertThat(
					Objects.requireNonNull(response.getResponseBody()).stream()
						.allMatch(allData -> allData.getAllData().stream()
							.noneMatch(issue -> issue.getStatus().isOpen()))
				).isEqualTo(true));
	}

	@Test
	void 잘못된_인자가_들어오는_경우_오류를_반환한다() {
		String url = "http://localhost:" + port + "/api/v1/issues?status=wrong";
		ErrorResponseDto responseBody = webTestClient.get()
			.uri(url)
			.exchange()
			.expectStatus()
			.is4xxClientError()
			.expectBody(ErrorResponseDto.class)
			.returnResult()
			.getResponseBody();
		assert responseBody != null;
		assertThat(responseBody.getMessage()).isEqualTo("wrong input but check");
	}
}