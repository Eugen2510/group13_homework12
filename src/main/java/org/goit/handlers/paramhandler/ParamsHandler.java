package org.goit.handlers.paramhandler;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ParamsHandler {
    private final HttpServletRequest req;
    private static final String PARAMETER_NAME = "timezone";

    public ParamsHandler(HttpServletRequest req) {
        this.req = req;
    }

    public boolean hasParameter(){
        return req.getParameter(PARAMETER_NAME) != null;
    }

    private String getRawParameter(){
        String rawParam = null;
        if (hasParameter()){
            rawParam = URLEncoder
                    .encode(req.getParameter(PARAMETER_NAME), StandardCharsets.UTF_8)
                    .toUpperCase()
                    .replace("%2B", "+");
        }
        return rawParam;
    }

    private boolean isParamValid() {
        boolean isValid = false;
        String parameter = getRawParameter();
        if (parameter != null){
            isValid = parameter.matches("(UTC\\+\\d+)" +
                    "|(UTC-\\d+)");
        }
        return isValid;
    }

    public String parseParam(){
        String timezone = "GMT";
        if (isParamValid()) {
            timezone = getRawParameter().replace("UTC", "GMT");
        }
        return timezone;
    }

    public int getHourOffset(){
        int offset = 0;
        String param = parseParam();
        if (isParamValid()){
            String hour = param.replace("GMT", "");
            offset = Integer.parseInt(hour);
        }
        return offset;
    }

    public String getTimeForOutput (String zone){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss 'UTC'X");
        TimeZone timeZone = TimeZone.getTimeZone(zone);
        dateFormat.setTimeZone(timeZone);
        String response = dateFormat.format(new Date());
        return response.replace("Z", "");
    }

}


