package com.epam.java.rt.lab.web.tag;

import com.epam.java.rt.lab.util.TimestampManager;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateValue extends SimpleTagSupport {

    private static final String TIME_FORMAT = "HH:mm:ss";

    private String stringValue;
    private Locale locale;

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public void doTag() throws JspException, IOException {
        DateFormat dateFormat = DateFormat.
                getDateInstance(DateFormat.MEDIUM, locale);
        if (TimestampManager.isTimestamp(stringValue)) {
            Timestamp timestamp = Timestamp.valueOf(stringValue);
            Date dateValue = new Date(timestamp.getTime());
            DateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
            JspWriter out = getJspContext().getOut();
            out.print("<small>" + dateFormat.format(dateValue)
                    + "</small> " + timeFormat.format(dateValue));
        }
    }

}
