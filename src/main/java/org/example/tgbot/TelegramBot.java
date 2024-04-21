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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private Map<Long, String> userEmails = new HashMap<>();
    private Map<Long, String> userPasswords = new HashMap<>();
    private Map<Long, Boolean> userRoles = new HashMap<>();
    private Map<Long, Boolean> userLoggedIn = new HashMap<>(); // New map to track login status

    @Override
    public String getBotUsername() {
        return "CTSoUA_bot";
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            Long chatId = message.getChatId();
            if (!userLoggedIn.containsKey(chatId) || !userLoggedIn.get(chatId)) {
                // Check if the user is not logged in or is not marked as logged in
                handleUnauthorizedUser(chatId, message.getText());
            } else {
                handleAuthorizedUser(chatId, message.getText());
            }
        }
    }

    @Override
    public String getBotToken() {
        return "6959322213:AAGjboBmXBH3PixnTip8bT6ISYRfIiI_c7E";
    }

    private void handleUnauthorizedUser(Long chatId, String text) {
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
            setUserRole(chatId, false);
            sendConfirmationMessage(chatId, "Ви успішно авторизувалися!");
            sendMainMenu(chatId);
            // Mark user as logged in
            userLoggedIn.put(chatId, true);
        }
    }

    private void handleAuthorizedUser(Long chatId, String text) {
        if ("Додати вантаж".equals(text)) {
            addCargo(chatId);
        } else if ("Видалити вантаж".equals(text)) {
            deleteCargo(chatId);
        } else if (!userRoles.get(chatId)) {
            if ("Вибрати перевізника".equals(text)) {
                menuTransporter(chatId,text);
            } else if ("Завершити перевезення".equals(text)) {
                // Logic for completing transportation
            }
//        } else if (userRoles.get(chatId)) {
//            if ("Вибрати замовлення".equals(text)){
//                //
//            } else if ("Завершити замовлення".equals(text)) {
//
//            }
        }
    }

    private void sendStartMessage(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Вітаю! Для початку увійдіть, натиснувши кнопку 'Увійти'.");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Увійти");
        keyboard.add(row);

        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);

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

    private void setUserRole(Long chatId, boolean isTransporter) {
        userRoles.put(chatId, isTransporter);
    }

    private void sendMainMenu(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Головне меню:");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Додати вантаж");
        row1.add("Видалити вантаж");
        keyboard.add(row1);

        if (!userRoles.get(chatId)) {
            KeyboardRow row2 = new KeyboardRow();
            row2.add("Вибрати перевізника");
            row2.add("Завершити перевезення");
            keyboard.add(row2);
        } else if (userRoles.get(chatId)){
            KeyboardRow row3 = new KeyboardRow();
            row3.add("Вибрати замовлення");
            row3.add("Завершити замовлення");
            keyboard.add(row3);
        }

        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void addCargo(Long chatId) {
        SendMessage nameMessage = new SendMessage();
        nameMessage.setChatId(chatId);
        nameMessage.setText("Введіть назву вантажу:");

        SendMessage weightMessage = new SendMessage();
        weightMessage.setChatId(chatId);
        weightMessage.setText("Введіть вагу вантажу (у кілограмах):");

        SendMessage originMessage = new SendMessage();
        originMessage.setChatId(chatId);
        originMessage.setText("Введіть адресу місця знаходження вантажу:");

        SendMessage destinationMessage = new SendMessage();
        destinationMessage.setChatId(chatId);
        destinationMessage.setText("Введіть адресу доставки вантажу:");

        try {
            execute(nameMessage);
            execute(weightMessage);
            execute(originMessage);
            execute(destinationMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    private  void deleteCargo(long chatId){
        SendMessage deleteMessage = new SendMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setText("введіть назву вантажу для видалення:");
        try {
            execute(deleteMessage);
        }catch (TelegramApiException e ){
            e.printStackTrace();
        }
    }

    private  void  menuTransporter(long chatId,String text){
        SendMessage menuTransporter = new SendMessage();
        menuTransporter.setChatId(chatId);
        menuTransporter.setText("список активних водіїв\n/1_oleg_m \n/2_ivan_r\n/3_nazar_b\n/4_ignat\n/5_lol \n/6_rick\n");


        try {
            execute(menuTransporter);

        }catch (TelegramApiException e){
            e.printStackTrace();
        }

    }
}