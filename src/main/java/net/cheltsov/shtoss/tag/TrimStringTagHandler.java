package net.cheltsov.shtoss.tag;

import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

/**
 * The tag provides possibility to cut string if number of characters
 * is more then necessary or do nothing otherwise
 */
public class TrimStringTagHandler extends SimpleTagSupport {
    private String value = new String();
    private Integer maxCharacters;
    private boolean dots = true;

    public void setValue(String value) {
        this.value = value;
    }

    public void setMaxCharacters(Integer maxCharacters) {
        this.maxCharacters = maxCharacters;
    }

    public void setDots(boolean dots) {
        this.dots = dots;
    }

    @Override
    public void doTag() throws IOException {
        if (value.length() > maxCharacters) {
            value = value.substring(0, maxCharacters);
        }
        if (dots) {
            value += "...";
        }
        getJspContext().getOut().println(value);
    }
}
