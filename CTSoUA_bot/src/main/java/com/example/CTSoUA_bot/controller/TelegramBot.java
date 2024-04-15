package com.example.CTSoUA_bot.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
@Component
public class TelegramBot extends TelegramLongPollingBot {
    @Value("${bot.name}")
    private String botName;
    @Value("${bot.token}")
    private String botToken;



    @Override
    public String getBotUsername() {
//        return botName;
        return "CTSoUA2_bot";
    }

    @Override
    public String getBotToken() {
//        return botToken;
        return "6836705058:AAEeoeY8Abgpe9082pVz0lL4YUnIfysjdTk";
    }

    @Override
    public void onUpdateReceived(Update update) {
        var originalMessage = update.getMessage();
        System.out.println(originalMessage.getText());

    }
}
