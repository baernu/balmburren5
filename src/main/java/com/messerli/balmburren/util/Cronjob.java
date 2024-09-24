package com.messerli.balmburren.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class Cronjob {

    @Scheduled(cron = "0 0 * * * ?")
    public void reportCurrentTime() {
       //todo
    }
}
