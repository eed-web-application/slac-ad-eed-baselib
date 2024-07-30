package edu.stanford.slac.ad.eed.baselib.config;

import edu.stanford.slac.ad.eed.baselib.exception.ControllerLogicException;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

/**
 * Automatically add field to be compliant with the
 * default REST API default dto.
 */
@Log4j2
@ControllerAdvice
public class ErrorAttributeConfiguration {
    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
                Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
                Throwable error = getError(webRequest);
                if (error != null) {
                    if (error instanceof ControllerLogicException myException) {
                        errorAttributes.put("errorCode", myException.getErrorCode());
                        errorAttributes.put("errorMessage", myException.getErrorMessage());
                        errorAttributes.put("errorDomain", myException.getErrorDomain());
                    } else if (error instanceof BindException bindExc) {
                        int errNum = 1;
                        for (ObjectError err : bindExc.getAllErrors()) {
                            errorAttributes.put(String.format("Err.%d", errNum++), err.toString());
                        }
                    } else {
                        errorAttributes.put(error.getClass().getName(), error.toString());
                    }
                    if(error.toString()!=null){
                        log.error("Error Message: {}", error.toString());
                    } else {
                        error.printStackTrace();
                        log.error("Error: {}", getStackTraceAsString(error));
                    }
                }
                return errorAttributes;
            }
        };
    }
    /**
     * Get the stack trace as a string
     * @param throwable the throwable
     * @return the stack trace as a string
     */
    private  String getStackTraceAsString(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
