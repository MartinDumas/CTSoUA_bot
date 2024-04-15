package com.example.CTSoUA_bot.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    @Value("${bot.name}")
    private String botName;
    @Value("${bot.token}")
    private String botToken;



    @Override
    public String getBotUsername() {
//        return botName;
        return "CTSoUA_bot";
    }

    @Override
    public String getBotToken() {
//        return botToken;
        return "6959322213:AAGjboBmXBH3PixnTip8bT6ISYRfIiI_c7E";
    }

    @Override
    public void onUpdateReceived(Update update) {
        var originalMessage = update.getMessage();
        System.out.println(originalMessage.getText());

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
