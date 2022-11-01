package com.github.gbaso.discordbot.config;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import discord4j.common.JacksonResources;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.RestClient;
import discord4j.rest.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class GlobalCommandRegistrar implements ApplicationRunner {

	private final RestClient client;
	private final JacksonResources resources = JacksonResources.create();

	@Override
	public void run(ApplicationArguments args) throws IOException {
		ApplicationService applicationService = client.getApplicationService();
		long applicationId = client.getApplicationId().block();

		var matcher = new PathMatchingResourcePatternResolver();
		List<ApplicationCommandRequest> commands = Stream.of(matcher.getResources("commands/*.json"))
				.map(this::readRequest)
				.toList();

		applicationService.bulkOverwriteGlobalApplicationCommand(applicationId, commands)
				.doOnNext(ignore -> log.debug("Successfully registered Global Commands"))
				.doOnError(e -> log.error("Failed to register global commands", e))
				.subscribe();
	}

	@SneakyThrows
	ApplicationCommandRequest readRequest(Resource resource) {
		return resources.getObjectMapper()
				.readValue(resource.getInputStream(), ApplicationCommandRequest.class);
	}

}
