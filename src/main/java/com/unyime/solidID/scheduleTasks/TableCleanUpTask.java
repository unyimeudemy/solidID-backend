package com.unyime.solidID.scheduleTasks;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TableCleanUpTask {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TableCleanUpTask(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Scheduled(fixedRate = 1000 * 60 * 60 * 24, initialDelay = 1000 * 60 * 60 * 24)
    public void deleteRecordsOlderThan24HrsFromIdentityUrls(){

        LocalDateTime twentyFourHoursAgo = LocalDateTime.now().minusHours(24);
        jdbcTemplate.update("DELETE FROM identity_urls WHERE date < ?", twentyFourHoursAgo);
    }
}
