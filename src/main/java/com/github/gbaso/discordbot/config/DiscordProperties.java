package com.github.gbaso.discordbot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("discord")
public record DiscordProperties(DiscordProperties.Application application) {

	public static record Application(String id, String publicKey, String token) {}

}
