package ar.edu.itba.paw.model;

import org.apache.commons.text.StringEscapeUtils;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jsoup.Jsoup;

public class TextUtils {

    private static final int WPM = 265;
    public static void main(String... args) {
        String markdownValue = "# heading h1\n"
                + "## heading h2\n"
                + "### heading h3\n"
                + "#### heading h4\n"
                + "---";

        String htmlValue = convertMarkdownToHTML(StringEscapeUtils.escapeHtml4(markdownValue));

        System.out.println("Markdown String:");
        System.out.println(markdownValue);
        System.out.println("HTML String:");
        System.out.println(htmlValue);
    }

    public static String convertMarkdownToHTML(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(StringEscapeUtils.escapeHtml4(markdown));
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
