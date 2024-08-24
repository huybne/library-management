package com.ensas.librarymanagementsystem.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoggedUserListener implements ApplicationListener<SessionDestroyedEvent> {

    private ActiveUserStore activeUserStore;

    @Autowired
    public LoggedUserListener(ActiveUserStore activeUserStore) {
        this.activeUserStore = activeUserStore;
    }

    @Override
    public void onApplicationEvent(SessionDestroyedEvent event) {
        List<SecurityContext> securityContexts = event.getSecurityContexts();
        for (SecurityContext securityContext : securityContexts) {
            String username = securityContext.getAuthentication().getName();
            activeUserStore.users.remove(username);
        }
    }
}
