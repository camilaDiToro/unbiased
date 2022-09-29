package ar.edu.itba.paw.model.news;

import org.apache.commons.text.StringEscapeUtils;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jsoup.Jsoup;

public class TextUtils {

    private static final int WPM = 265;

    private TextUtils() {

    }

    public static void main(String[] args) {
        System.out.println(convertMarkdownToHTML("# hola"));
    }

    public static String convertMarkdownToHTML(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(StringEscapeUtils.escapeHtml4(markdown));
//        Node document = parser.parse(markdown);
//        return StringEscapeUtils.escapeHtml4(markdown);

        HtmlRenderer htmlRenderer = HtmlRenderer.builder().build();
        return htmlRenderer.render(document);
    }

    public static String extractTextFromHTML(String html) {
        return Jsoup.parse(html).wholeText();
    }

    public static int estimatedMinutesToRead(String text) {
        return (int)Math.ceil(text.split("\\s+").length / (double)WPM);
    }
}
