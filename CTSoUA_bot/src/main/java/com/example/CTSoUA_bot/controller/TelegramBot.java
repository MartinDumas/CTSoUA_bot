package com.example.CTSoUA_bot.controller;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramBot extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return "CTSoUA_bot";
    }

    @Override
    public String getBotToken() {
        return "7059758535:AAHfTWI3LSZrqEAhJRl5iSVvmXCMP9Y67ds";
    }

    @Override
    public void onUpdateReceived(Update update) {

    }
}
