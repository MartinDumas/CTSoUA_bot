package com.example.CTSoUA_bot;

import com.example.CTSoUA_bot.controller.TelegramBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class CtSoUaBotApplication {

	public static void main(String[] args) throws TelegramApiException {
		SpringApplication.run(CtSoUaBotApplication.class, args);
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
		telegramBotsApi.registerBot(new TelegramBot());

	}

	}


