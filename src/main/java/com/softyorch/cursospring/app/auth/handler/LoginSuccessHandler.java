package com.softyorch.cursospring.app.auth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.SessionFlashMapManager;

import java.io.IOException;
import java.util.Locale;

@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private LocaleResolver localeResolver;

    @Autowired
    private MessageSource messages;
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        SessionFlashMapManager flashMapManager = new SessionFlashMapManager();

        Locale locale = localeResolver.resolveLocale(request);
        String message = String.format(
                messages.getMessage("text.login.success", null, locale), authentication.getName()
        );
        FlashMap flashMap = new FlashMap();
        flashMap.put("success", message);

        flashMapManager.saveOutputFlashMap(flashMap, request, response);

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
