package it.gov.pagopa.bpd.citizen.service;

import it.gov.pagopa.bpd.citizen.command.SendAsyncEventCommand;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CitizenEventServiceImpl implements CitizenEventService {

    private BeanFactory beanFactory;

    @Autowired
    public CitizenEventServiceImpl(
            ObjectProvider<BeanFactory> beanFactory) {
        this.beanFactory = beanFactory.getIfAvailable();
    }

    @SneakyThrows
    @Override
    @Scheduled(cron = "${core.CitizenEventService.retrySendingEvents.scheduler}")
    @SchedulerLock(name = "CitizenEventServiceImpl.retrySendingEvents",
            lockAtMostFor = "${core.CitizenEventService.retrySendingEvents.lockAtMostFor}")
    public void retrySendingEvents() {
        log.debug("CitizenEventServiceImpl.retrySendingEvents");
        SendAsyncEventCommand sendAsyncEventCommand = beanFactory.getBean(
                SendAsyncEventCommand.class);
        sendAsyncEventCommand.execute();
    }


}
