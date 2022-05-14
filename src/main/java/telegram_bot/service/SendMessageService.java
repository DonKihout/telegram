package telegram_bot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import static java.util.Arrays.asList;
import static telegram_bot.constant.VarConstasnt.*;

public class SendMessageService {
    private final String greetingMessage = "Привет, друг! Я бот-помощник, " +
                                         "проконсультирую тебя \nпо поступлению в вуз!";

    private final String MAIN_INFO_MESSAGE = "Укажите предметы с которыми хотите поступить, а также общее количество" +
                                                "баллов ЕГЭ. \nНапример \"физика информатика биология 245\"";

    private final String DOCUMENTS_LIST_MESSAGE = "Для поступления в наш университет необходимы следующие документы в скан-форме:\n" +
                                                    "Паспорт\nСНИЛС\nДокумент об образовании\nХотите загрузить их? (Да/Нет)";

    private final String[] QUESTIONS = {"Ваши ФИО полностью?",
                                         "Ваш год рождения?",
                                        "Ваше текущее образование?",
                                         "Желаемая специальность?",
                                          "Количество баллов ЕГЭ?"};
    public boolean flag = true;
    private int i;
    private ButtonsService buttonsService = new ButtonsService();

    public SendMessage createGreeting(Update update){
        SendMessage message = createMessage(update, greetingMessage);
        ReplyKeyboardMarkup keyboardMarkup = buttonsService.setButtons
                                         (buttonsService.createButtons(asList(INFO, STATEMENT, DOCUMENTS)));
        message.setReplyMarkup(keyboardMarkup);
        return message;
    }

    public SendMessage infoMessage(Update update){
        SendMessage message = createMessage(update, MAIN_INFO_MESSAGE);
        return message;
    }

    public SendMessage sendSpecializationsInfo(Update update, String[] specializations){
        SendMessage message = createMessage(update, MAIN_INFO_MESSAGE);
        return message;
    }

    public SendMessage sendStatementQuestions(Update update){
        try{
            SendMessage message = createMessage(update, QUESTIONS[i]);
            i++;
            return message;
        } catch (ArrayIndexOutOfBoundsException e){
            return null;
        }
    }

    public SendMessage sendDocumentsInfo(Update update){
        SendMessage message = createMessage(update, DOCUMENTS_LIST_MESSAGE);
        return message;
    }

    public SendMessage createMessage(Update update, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
        sendMessage.setText(message);
        return sendMessage;
    }

    public SendMessage createMessage(String chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        return sendMessage;
    }

    public void setI(int i) {
        this.i = i;
    }
}
