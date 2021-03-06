package com.codesquad.issue04.domain.issue.vo.firstcollection;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.codesquad.issue04.domain.issue.vo.Comment;
import com.codesquad.issue04.web.dto.request.comment.CommentUpdateRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(exclude = "comments")
@Embeddable
public class Comments {
	@OneToMany(mappedBy = "issue", orphanRemoval = true, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Comment> comments = new ArrayList<>();

	protected Comments() {
	}

	private Comments(List<Comment> comments) {
		this.comments = comments;
	}

	public static Comments ofNullComments() {
		return new Comments();
	}

	public static Comments ofComments(List<Comment> comments) {
		return new Comments(comments);
	}

	public List<Comment> returnCommentsCreatingNewList() {
		return new ArrayList<>(this.comments);
	}

	@JsonIgnore
	public Comment getOverview() {
		if (this.comments.size() == 0) {
			return Comment.ofNullComment();
		}
		return this.comments.get(0);
	}

	public int getCommentsSize() {
		return this.comments.size();
	}

	public Comment getCommentByIndex(int commentIndex) {
		return this.comments.get(commentIndex);
	}

	@JsonIgnore
	public Comment getLatestComment() {
		int latestIndex = this.comments.size() - 1;
		return this.comments.get(latestIndex);
	}

	public Comment findCommentById(Long commentId) {
		return comments.stream()
			.filter(comment -> comment.doesMatchId(commentId))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("not found"));
	}

	public Comment addComment(Comment comment) {
		this.comments.add(comment);
		return comment;
	}

	public Comment deleteCommentById(Long commentId) {
		Comment deletedComment = findCommentById(commentId);
		this.comments.remove(deletedComment);
		return deletedComment;
	}

	public Comment modifyCommentByDto(CommentUpdateRequestDto dto) {
		Comment asIs = comments.stream()
			.filter(comment -> comment.doesMatchId(dto.getCommentId()))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("not able to find"));
		asIs.updateComment(dto);
		return asIs;
	}

	public boolean hasUserId(String userId) {
		return this.comments.stream()
			.anyMatch(comment -> comment.isSameAuthor(userId));
	}
}
