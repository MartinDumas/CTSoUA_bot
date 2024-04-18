package org.example.tgbot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private boolean isFirstMessage = true;

    @Override
    public String getBotUsername() {
        return "CTSoUA_bot";
    }

    @Override
    public void onUpdateReceived(Update update) {
        // Отримання тексту повідомлення від користувача
        String messageText = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();

        // Виведення вітального повідомлення при старті бота
        if (isFirstMessage) {
            sendWelcomeMessage(chatId);
            isFirstMessage = false;
            return;
        }


        if (messageText.equals("Увійти")) {
            sendLoginInstructions(chatId);
        }
    }

    private void sendWelcomeMessage(long chatId) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Вас вітає система перевезення вантажів української армії." +
                "\n Будь ласка, увійдіть у свій профіль!");
        sendMessage.enableMarkdown(true);
        ReplyKeyboardMarkup keyboardMarkup = getMenuKeyboard();
        sendMessage.setReplyMarkup(keyboardMarkup);

        try {
            execute(sendMessage); // Відправлення вітального повідомлення
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendLoginInstructions(long chatId) {
        SendMessage loginMessage = new SendMessage();
        loginMessage.setChatId(String.valueOf(chatId));
        loginMessage.setText("Будь ласка, введіть свою електронну адресу");

        try {
            execute(loginMessage); // Відправлення повідомлення про вхід
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private ReplyKeyboardMarkup getMenuKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add("Увійти");
        keyboardRows.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        return replyKeyboardMarkup;
    }

    @Override
    public String getBotToken() {
        return "6959322213:AAGjboBmXBH3PixnTip8bT6ISYRfIiI_c7E";
    }
}
