package telegram_bot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import telegram_bot.core.Core;

import java.util.Collection;
import java.util.LinkedList;

import static telegram_bot.constant.VarConstasnt.*;

public class Spmi {
    public static Collection<String> buttons = new LinkedList<String>();

    public static void main(String[] args) {
        buttons.add(START);
        buttons.add(INFO);
        buttons.add(DOCUMENTS);
        buttons.add(STATEMENT);

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new Core());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
