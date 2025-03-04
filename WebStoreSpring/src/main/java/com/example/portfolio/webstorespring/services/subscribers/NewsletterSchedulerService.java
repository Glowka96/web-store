package com.example.portfolio.webstorespring.services.subscribers;

import com.example.portfolio.webstorespring.models.entity.subscribers.NewsletterMessage;
import com.example.portfolio.webstorespring.services.emails.notifications.NotifyNewsletterSubscriberService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
public class NewsletterSchedulerService {

    private final NewsletterMessageService newsletterMessageService;
    private final NotifyNewsletterSubscriberService notifyNewsletterSubscriberService;
    private final TaskScheduler taskScheduler;
    private final Clock clock;

    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    @Scheduled(fixedRate = 3_600_000, initialDelay = 10_000)
    public void checkAndScheduleTasks() {
        List<NewsletterMessage> futureMessages = newsletterMessageService.getAllFutureMessages();

        for (NewsletterMessage message : futureMessages) {
            scheduleTask(message);
        }
    }

    private void scheduleTask(NewsletterMessage newsletterMessage) {
        if (scheduledTasks.containsKey(newsletterMessage.getId())) {
            return;
        }

        ScheduledFuture<?> future = taskScheduler.schedule(
                () -> executeTask(newsletterMessage), newsletterMessage.getSendDate().atZone(clock.getZone()).toInstant()
        );
        scheduledTasks.put(newsletterMessage.getId(), future);
    }

    private void executeTask(NewsletterMessage newsletterMessage) {
        notifyNewsletterSubscriberService.notifyEnabledSubscribers(newsletterMessage.getMessage());
        scheduledTasks.remove(newsletterMessage.getId());
    }
}
