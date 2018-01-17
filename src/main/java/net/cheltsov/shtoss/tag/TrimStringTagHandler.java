package net.cheltsov.shtoss.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class TrimStringTagHandler extends SimpleTagSupport {
    private String value = new String();
    private Integer maxCharacters;

    public void setValue(String value) {
        this.value = value;
    }

    public void setMaxCharacters(Integer maxCharacters) {
        this.maxCharacters = maxCharacters;
    }

    @Override
    public void doTag() throws JspException, IOException {
        if (value.length() > maxCharacters) {
            value = value.substring(0, maxCharacters) + "...";
        }
        getJspContext().getOut().println(value);
    }
}
