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
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

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
        else if (messageText.equals("Зареєструватися")) {
            sendRegistrationInstructions(chatId);
        }
        else if (!messageText.equals("error")) {
            sendError(chatId);
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
        loginMessage.setText("Будь ласка, введіть свою електронну адресу");// я б казав вводити по номеру телефону бо він є точно у всіх
//        loginMessage.setText("Будь ласка, введіть свой номер телефону");

        try {
            execute(loginMessage); // Відправлення повідомлення про вхід
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendRegistrationInstructions(long chatId){
        SendMessage registrationMessage = new SendMessage();
        registrationMessage.setChatId(String.valueOf(chatId));
        registrationMessage.setText("Якщо ви вперше користуєтеся ботом введіть будь ласка пошту та пароль");
        try {
            execute(registrationMessage); // Відправлення повідомлення про вхід
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    private void sendError(long chatId){
        SendMessage errorMessage = new SendMessage();
        errorMessage.setChatId(String.valueOf(chatId));
        errorMessage.setText("Неправильно введена інформація");
        try {
            execute(errorMessage); // Відправлення повідомлення про вхід
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
        keyboardRow.add("Зареєструватися");

        keyboardRows.add(keyboardRow);

        replyKeyboardMarkup.setKeyboard(keyboardRows);

        return replyKeyboardMarkup;
    }

    @Override
    public String getBotToken() {
        return "6959322213:AAGjboBmXBH3PixnTip8bT6ISYRfIiI_c7E";
    }
}
