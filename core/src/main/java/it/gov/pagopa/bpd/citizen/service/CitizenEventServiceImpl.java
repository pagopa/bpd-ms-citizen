package it.gov.pagopa.bpd.citizen.service;

import it.gov.pagopa.bpd.citizen.command.SendAsyncEventCommand;
import lombok.SneakyThrows;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
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
    public void retrySendingEvents(String fiscalCode) {
        SendAsyncEventCommand sendAsyncEventCommand = beanFactory.getBean(
                SendAsyncEventCommand.class, fiscalCode);
        sendAsyncEventCommand.execute();
    }


}
