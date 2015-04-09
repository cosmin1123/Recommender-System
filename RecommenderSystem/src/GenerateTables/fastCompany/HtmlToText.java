package GenerateTables.fastCompany;
import java.io.*;
import javax.swing.text.html.*;
import javax.swing.text.html.parser.*;

/**
 * Created by didii on 3/25/15.
 */

public class HtmlToText extends HTMLEditorKit.ParserCallback {
    StringBuffer s;

    public void parse(String str) {
        s = new StringBuffer();
        ParserDelegator delegator = new ParserDelegator();


        try {
            delegator.parse(new StringReader(str), this, Boolean.TRUE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleText(char[] text, int pos) {
        s.append(text);
    }

    public String getText() {
        return s.toString();
    }
}