package it.course.myblogc4.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import it.course.myblogc4.repository.LogoutTraceRepository;

@Service
public class TokensPurgeTask {

    @Autowired
    private LogoutTraceRepository logoutTraceRepository;

    @Scheduled(cron = "${purge.cron.expression}")
    public void purgeExpired() {
    	logoutTraceRepository.deleteExpiredTokens();
    }
}
