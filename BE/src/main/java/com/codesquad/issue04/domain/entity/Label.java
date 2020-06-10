package com.codesquad.issue04.domain.entity;

import com.codesquad.issue04.domain.firstcollections.Issues;
import lombok.*;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
public class Label {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    private String description;
    @Embedded
    private Issues issueList;
}