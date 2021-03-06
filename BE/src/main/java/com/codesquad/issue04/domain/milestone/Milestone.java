package com.codesquad.issue04.domain.milestone;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.codesquad.issue04.domain.issue.Issue;
import com.codesquad.issue04.web.dto.request.milestone.MilestoneCreateRequestDto;
import com.codesquad.issue04.web.dto.request.milestone.MilestoneUpdateRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString(exclude = "issues")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Builder
public class Milestone implements AbstractMilestone {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private LocalDate dueDate;
	private String description;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	@JoinTable(
		name = "milestone_has_issue",
		joinColumns = @JoinColumn(name = "milestone_id"),
		inverseJoinColumns = @JoinColumn(name = "issue_id")
	)
	private List<Issue> issues = new ArrayList<>();

	@Override
	public boolean isNil() {
		return false;
	}

	public Milestone updateMilestone(final MilestoneUpdateRequestDto dto) {
		this.title = dto.getTitle();
		this.dueDate = dto.getDueDate();
		this.description = dto.getDescription();
		return this;
	}

	public void initializeIssue() {
		issues.clear();
	}

	public static Milestone of(MilestoneCreateRequestDto milestoneCreateRequestDto) {
		return Milestone.builder()
			.title(milestoneCreateRequestDto.getTitle())
			.dueDate(milestoneCreateRequestDto.getDueDate())
			.description(milestoneCreateRequestDto.getDescription())
			.build();
	}
}
