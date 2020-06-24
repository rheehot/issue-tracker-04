package com.codesquad.issue04.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.issue04.domain.label.Label;
import com.codesquad.issue04.domain.label.LabelRepository;
import com.codesquad.issue04.web.dto.request.LabelCreateRequestDto;
import com.codesquad.issue04.web.dto.request.LabelDeleteRequestDto;
import com.codesquad.issue04.web.dto.request.LabelUpdateRequestDto;
import com.codesquad.issue04.web.dto.response.label.LabelDetailResponseDto;
import com.codesquad.issue04.web.dto.response.label.LabelOverviewDto;
import com.codesquad.issue04.web.dto.response.label.LabelOverviewResponseDtos;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LabelService {

	private final LabelRepository labelRepository;

	protected Label findLabelById(Long labelId) {
		return labelRepository.findById(labelId)
			.orElseThrow(() -> new IllegalArgumentException("label not found id: " + labelId));
	}

	protected List<Label> findAllLabels() {
		return labelRepository.findAll();
	}

	protected List<LabelOverviewDto> findAllLabelsOverview() {
		List<Label> labels = findAllLabels();
		return labels.stream()
			.map(LabelOverviewDto::of)
			.collect(Collectors.toList());
	}

	public LabelDetailResponseDto findLabelDetailById(Long labelId) {
		return LabelDetailResponseDto.of(findLabelById(labelId));
	}

	public LabelOverviewResponseDtos getLabelOverviews() {
		return LabelOverviewResponseDtos.builder()
			.allData(findAllLabelsOverview())
			.build();
	}

	@Transactional
	public LabelDetailResponseDto findLatestIssue() {
		Label latestLabel = labelRepository.findTopByOrderByIdDesc();
		return LabelDetailResponseDto.of(latestLabel);
	}

	@Transactional
	public Label createNewLabel(LabelCreateRequestDto dto) {
		Label savedLabel = Label.builder()
			.title(dto.getTitle())
			.color(dto.getColor())
			.description(dto.getDescription())
			.build();
		return savedLabel;
	}

	public Label updateExistingLabel(LabelUpdateRequestDto dto) {
		Label updatedLabel = findLabelById(dto.getId());
		updatedLabel.updateLabel(dto);
		return updatedLabel;
	}

	public Label deleteExistingLabel(LabelDeleteRequestDto dto) {
		Label deletedLabel = findLabelById(dto.getId());
		labelRepository.delete(deletedLabel);
		return deletedLabel;
	}
}