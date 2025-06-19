package com.backwishlist.infrastructure.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MessageResolver {

    private static MessageSource messageSource;

    public MessageResolver(MessageSource messageSource) {
        MessageResolver.messageSource = messageSource;
    }

    public static String get(String code, Object... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    public static String get(String code) {
        return get(code, null);
    }

}
