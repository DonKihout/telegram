package telegram_bot.core;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import telegram_bot.CreateWord;
import telegram_bot.service.SendMessageService;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static telegram_bot.Spmi.buttons;
import static telegram_bot.constant.VarConstasnt.*;

public class Core extends TelegramLongPollingBot {
    private SendMessageService sendMessageService = new SendMessageService();
    private CreateWord createWord = new CreateWord();
    private int code;
    private Set<String> users = new HashSet<>();
    private final String adminChatId = "419065441";
    private ArrayList<String> answers = new ArrayList<String>();

    @Override
    public void onUpdateReceived(Update update) {
        if (!String.valueOf(update.getMessage().getChatId()).equals(adminChatId)) {
            users.add(String.valueOf(update.getMessage().getChatId()));
        }
        if (String.valueOf(update.getMessage().getChatId()).equals(adminChatId) && !buttons.contains(update.getMessage().getText())) {
            users.forEach((user) -> {
                try {
                    execute(sendMessageService.createMessage(user, update.getMessage().getText()));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            });
        }
        if (update.hasMessage() && update.getMessage().hasText() | update.getMessage().hasDocument() | update.getMessage().hasPhoto()) {
            if (update.getMessage().hasDocument() && code == 2) {
                String fileId = update.getMessage().getDocument().getFileId();
                String fileName = update.getMessage().getDocument().getFileName();
                try {
                    downloadImages(fileId, fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            switch (update.getMessage().getText()) {
                case START:
                    try {
                        execute(sendMessageService.createGreeting(update));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case INFO:
                    try {
                        code = 1;
                        execute(sendMessageService.infoMessage(update));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case STATEMENT:
                    try {
                        code = 3;
                        sendMessageService.setI(0);
                        execute(sendMessageService.sendStatementQuestions(update));
                        sendMessageService.flag = false;
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }

                    break;
                case DOCUMENTS:
                    try {
                        code = 2;
                        execute(sendMessageService.sendDocumentsInfo(update));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    if (code == 1) {
                        String[] specialization = update.getMessage().getText().split(" +").clone();
                        if (specialization.length == 3) {
                            sendMessageService.sendSpecializationsInfo(update, specialization);
                        } else {
                            throw new ArrayIndexOutOfBoundsException();
                        }
                    }
                    if (code == 2) {
                        if (update.getMessage().getText().equalsIgnoreCase("ДА")) {
                            try {
                                execute(sendMessageService.createMessage(update, "Загружайте по отдельности. Ждемс..."));
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (code == 3) {
                        answers.add(update.getMessage().getText());
                        try {
                            execute(sendMessageService.sendStatementQuestions(update));
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        if (answers.size() == 5) {
                            SendDocument sendDocument = new SendDocument();
                            String name = answers.get(0).split(" ")[0];
                            sendDocument.setDocument(new InputFile(new File(createWord.wordFileCreation(answers)), name + "_statement.docx"));
                            sendDocument.setChatId(String.valueOf(update.getMessage().getChatId()));
                            try {
                                execute(sendDocument);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                            answers.clear();
                        }
                    }
            }
        }
    }


    public void downloadImages(String fileId, String fileName) throws IOException {
        URL url = new URL("https://api.telegram.org/bot" + getBotToken() + "/getFile?file_id=" + fileId);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
        String getFileResponse = bufferedReader.readLine();
        JSONObject jresult = new JSONObject(getFileResponse);
        JSONObject path = jresult.getJSONObject("result");
        String file_path = path.getString("file_path");
        File localeFile = new File("src/main/resources/telegram/" + fileName);
        InputStream inputStream = new URL("https://api.telegram.org/file/bot" + getBotToken() + "/" + file_path).openStream();
        FileUtils.copyInputStreamToFile(inputStream, localeFile);
        bufferedReader.close();
        inputStream.close();
    }

    @Override
    public String getBotUsername() {
        return "@S_P_M_I_BOT";
    }

    @Override
    public String getBotToken() {
        return "5339683298:AAFA6YuASjCvIe2s6IPkjZCRZhfymF8hQTM";
    }

}
