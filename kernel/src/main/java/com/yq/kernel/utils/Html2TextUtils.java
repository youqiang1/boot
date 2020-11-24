package com.yq.kernel.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;
import java.io.*;

/**
 * <p> html转text</p>
 * @author youq  2020/9/30 11:11
 */
@Slf4j
@Getter
@Setter
public class Html2TextUtils extends HTMLEditorKit.ParserCallback {

    private static Html2TextUtils html2TextUtils = new Html2TextUtils();

    private StringBuffer s;

    private char[] text;

    private int pos;

    public Html2TextUtils() {}

    public void parse(String str) throws IOException {
        InputStream iin = new ByteArrayInputStream(str.getBytes());
        Reader in = new InputStreamReader(iin);
        s = new StringBuffer();
        ParserDelegator delegator = new ParserDelegator();
        // the third parameter is TRUE to ignore charset directive
        delegator.parse(in, this, Boolean.TRUE);
        iin.close();
        in.close();
    }

    @Override
    public void handleText(char[] text, int pos) {
        s.append(text);
    }

    public String getText() {
        return s.toString();
    }

    public static String getContent(String str) {
        try {
            html2TextUtils.parse(str);
        } catch (IOException e) {
            log.error("转换异常：", e);
        }
        return html2TextUtils.getText();
    }

}
