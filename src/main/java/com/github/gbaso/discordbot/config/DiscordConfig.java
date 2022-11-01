package com.github.gbaso.discordbot.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.presence.ClientActivity;
import discord4j.core.object.presence.ClientPresence;
import discord4j.rest.RestClient;

@EnableConfigurationProperties(DiscordProperties.class)
@Configuration
public class DiscordConfig {

	@Bean
	GatewayDiscordClient gatewayDiscordClient(DiscordProperties discord) {
		return DiscordClientBuilder.create(discord.application().token()).build()
				.gateway()
				.setInitialPresence(ignore -> ClientPresence.online(ClientActivity.listening("to /commands")))
				.login()
				.block();
	}

	@Bean
	RestClient discordRestClient(GatewayDiscordClient client) {
		return client.getRestClient();
	}

}
