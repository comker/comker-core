package net.cokkee.comker.service.impl;

import java.util.Locale;
import net.cokkee.comker.service.ComkerLocaleMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;

/**
 *
 * @author drupalex
 */
public class ComkerLocaleMessageServiceImpl 
        implements MessageSourceAware, ComkerLocaleMessageService {

    public static final Locale LOCALE_VI_VN = new Locale("vi", "VN");

    public static final Locale LOCALE_DEFAULT = Locale.US;

    @Autowired
    private MessageSource messageSource;

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public String getMessage (String code, String defaultMessage){
        return messageSource.getMessage(code, null, LOCALE_VI_VN);
    }
    
    @Override
    public String getMessage (String code, Object[] args, String defaultMessage){
        return messageSource.getMessage(code, args, LOCALE_VI_VN);
    }
}