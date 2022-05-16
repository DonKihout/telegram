package telegram_bot;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.FileOutputStream;
import java.util.ArrayList;

public class CreateWord {
    private final String DOCX_NAME = "statement.docx";

    public String wordFileCreation(ArrayList <String> answers) {

        XWPFDocument docxModel = new XWPFDocument();
        try {
            String name = answers.get(0).split(" ")[0];
            XWPFParagraph bodyParagraph = docxModel.createParagraph();
            bodyParagraph.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun paragraphConfig = bodyParagraph.createRun();
            paragraphConfig.setItalic(true);
            paragraphConfig.setFontSize(20);
            paragraphConfig.setColor("000000");
            paragraphConfig.setText(
                    "Я, " + answers.get(answers.size()-5) +"," + answers.get(answers.size()-4) + " года рождения, имею " + answers.get(answers.size()-3) +
                            " образование, настоятельно рекомендую рассмотреть мою кандидатуру на поступление в Ваш университет" +
                            " на специальность \"" + answers.get(answers.size()-2) + "\". Общее количество баллов ЕГЭ: " + answers.get(answers.size()-1)
            );
            FileOutputStream outputStream = new FileOutputStream("src/main/resources/telegram/" + name + "_" + DOCX_NAME);
            docxModel.write(outputStream);
            outputStream.close();
            return "src/main/resources/telegram/" + name + "_" + DOCX_NAME;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
