package com.github.gbaso.discordbot.commands;

import org.springframework.stereotype.Component;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import reactor.core.publisher.Mono;

@Component
public class GreetCommand implements SlashCommand {

	@Override
	public String getName() {
		return "greet";
	}

	@Override
	public Mono<Void> handle(ChatInputInteractionEvent event) {
		String name = event.getOption("name")
				.flatMap(ApplicationCommandInteractionOption::getValue)
				.map(ApplicationCommandInteractionOptionValue::asString)
				.orElseThrow();
		return event.reply()
				.withEphemeral(true)
				.withContent("Hello, " + name + "!");
	}

}
