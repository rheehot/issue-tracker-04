package com.codesquad.issue04.config.dto;

import java.util.Map;
import java.util.Optional;

import com.codesquad.issue04.domain.user.RealUser;
import com.codesquad.issue04.domain.user.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributes {

	private Map<String, Object> attributes;
	private String nameAttributeKey;
	private String name;
	private String githubId;
	private String image;

	@Builder
	public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String githubId,
		String image) {

		this.attributes = attributes;
		this.nameAttributeKey = nameAttributeKey;
		this.name = name;
		this.githubId = githubId;
		this.image = image;
	}

	public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
		return ofGithub(userNameAttributeName, attributes);
	}

	private static OAuthAttributes ofGithub(String userNameAttributeName, Map<String, Object> attributes) {
		return OAuthAttributes.builder()
			.name((String) Optional.ofNullable(attributes.get("name")).orElse(attributes.get("login")))
			.githubId((String) attributes.get("login"))
			.image((String) attributes.get("avatar_url"))
			.attributes(attributes)
			.nameAttributeKey(userNameAttributeName)
			.build();
	}

	public RealUser toEntity() {
		return RealUser.builder()
			.name(name)
			.githubId(githubId)
			.image(image)
			.role(Role.USER)
			.build();
	}
}