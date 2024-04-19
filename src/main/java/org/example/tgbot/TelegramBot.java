package org.example.tgbot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private Map<Long, String> userEmails = new HashMap<>();
    private Map<Long, String> userPasswords = new HashMap<>();

    @Override
    public String getBotUsername() {
        return "CTSoUA_bot";
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId = update.getMessage().getChatId();
        if (!isAuthorized(chatId)) {
            Message message = update.getMessage();
            if (message != null && message.hasText()) {
                String text = message.getText();
                if ("/start".equals(text)) {
                    sendStartMessage(chatId);
                } else if ("Увійти".equals(text)) {
                    if (!userEmails.containsKey(chatId)) {
                        sendPromptMessage(chatId, "Будь ласка, введіть електронну пошту:");
                    } else if (!userPasswords.containsKey(chatId)) {
                        sendPromptMessage(chatId, "Будь ласка, введіть пароль:");
                    }
                } else if (!userEmails.containsKey(chatId)) {
                    userEmails.put(chatId, text);
                    sendPromptMessage(chatId, "Будь ласка, введіть пароль:");
                } else if (!userPasswords.containsKey(chatId)) {
                    userPasswords.put(chatId, text);
                    setAuthorized(chatId);
                    sendConfirmationMessage(chatId, "Ви успішно авторизувалися!");
                }
            }
        } else {
            // Тут можна реалізувати додатковий функціонал для авторизованих користувачів
        }
    }

    @Override
    public String getBotToken() {
        return "6959322213:AAGjboBmXBH3PixnTip8bT6ISYRfIiI_c7E";
    }

    private void sendStartMessage(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Вітаю! Для початку увійдіть, натиснувши кнопку 'Увійти'.");

        // Встановлення клавіатури з кнопкою "Увійти"
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Увійти");
        keyboard.add(row);


        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    private void sendPromptMessage(Long chatId, String prompt) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(prompt);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendConfirmationMessage(Long chatId, String confirmation) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(confirmation);


        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private boolean isAuthorized(Long chatId) {
        return userEmails.containsKey(chatId) && userPasswords.containsKey(chatId);
    }

    private void setAuthorized(Long chatId) {
        userEmails.remove(chatId);
        userPasswords.remove(chatId);
    }
}
