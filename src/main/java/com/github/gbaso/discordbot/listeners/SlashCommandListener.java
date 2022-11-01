package com.github.gbaso.discordbot.listeners;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.github.gbaso.discordbot.commands.SlashCommand;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class SlashCommandListener {

	private final Map<String, SlashCommand> commands;

	public SlashCommandListener(GatewayDiscordClient client, Collection<SlashCommand> commands) {
		this.commands = commands.stream().collect(Collectors.toMap(SlashCommand::getName, Function.identity()));

		client.on(ChatInputInteractionEvent.class, this::handle).subscribe();
	}

	private Mono<Void> handle(ChatInputInteractionEvent event) {
		return Flux.just(event.getCommandName())
				.map(commands::get)
				.next()
				.flatMap(command -> command.handle(event));
	}

}
