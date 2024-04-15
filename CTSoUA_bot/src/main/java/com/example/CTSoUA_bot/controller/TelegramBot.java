package com.example.CTSoUA_bot.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
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
        String chatId = update.getMessage().getChatId().toString();
        String text = update.getMessage().getText();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);

        if (text.equals("/start")) {
            sendMessage.setText("ви розпочали роботу з ботом");
        } else if (text.equals("/help")) {
            sendMessage.setText("help");
        }


        try {
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }


    }
}
