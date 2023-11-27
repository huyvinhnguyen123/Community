package Blind.Sight.community.domain.service.system;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSynchronizationScheduler {
    private final DataSynchronizationService dataSynchronizationService;

    @Scheduled(fixedDelay = 3600000) // Synchronize every hour
    public void synchronizeData() {
        dataSynchronizationService.synchronizeMySQLUserData();
        dataSynchronizationService.synchronizeMySQLUserDataUpdate();
    }
}
