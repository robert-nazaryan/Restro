package org.restro.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.restro.entity.WeeklyEmail;
import org.restro.repository.WeeklyEmailRepository;
import org.restro.service.WeeklyEmailService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeeklyEmailServiceImpl implements WeeklyEmailService {

    private final WeeklyEmailRepository weeklyEmailRepository;

    @Override
    public void save(WeeklyEmail weeklyEmail) {
        weeklyEmailRepository.save(weeklyEmail);
        log.info("WeeklyEmail saved: {}", weeklyEmail);
    }

}
