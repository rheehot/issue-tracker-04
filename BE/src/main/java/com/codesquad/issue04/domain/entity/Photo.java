package com.codesquad.issue04.domain.entity;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Photo {

    private String url;
    private String githubId;
    private Long commentId;
}