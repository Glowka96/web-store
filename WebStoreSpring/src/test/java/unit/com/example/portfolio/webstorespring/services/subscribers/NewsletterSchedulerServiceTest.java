package com.example.portfolio.webstorespring.services.subscribers;

import com.example.portfolio.webstorespring.models.entity.subscribers.NewsletterMessage;
import com.example.portfolio.webstorespring.services.emails.notifications.NotifyNewsletterSubscriberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.TaskScheduler;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import static com.example.portfolio.webstorespring.buildhelpers.DateForTestBuilderHelper.LOCAL_DATE_TIME;
import static com.example.portfolio.webstorespring.buildhelpers.DateForTestBuilderHelper.ZONED_DATE_TIME;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewsletterSchedulerServiceTest {

    @Mock
    private NewsletterMessageService newsletterMessageService;
    @Mock
    private NotifyNewsletterSubscriberService notifyNewsletterSubscriberService;
    @Mock
    private TaskScheduler taskScheduler;
    @Mock
    private Clock clock;
    @InjectMocks
    private NewsletterSchedulerService underTest;

    private NewsletterMessage newsletterMessage;

    @BeforeEach
    void setUp() {
        newsletterMessage = new NewsletterMessage();
        newsletterMessage.setId(1L);
        newsletterMessage.setSendDate(LOCAL_DATE_TIME);
        newsletterMessage.setMessage("Test Message");

    }

    @Test
    void shouldCheckAndScheduleTasks_whenFutureMessagesNotExist() {
        given(newsletterMessageService.getAllFutureMessages()).willReturn(Collections.emptyList());

        underTest.checkAndScheduleTasks();

        verify(newsletterMessageService, times(1)).getAllFutureMessages();
        verifyNoMoreInteractions(taskScheduler, notifyNewsletterSubscriberService);
    }

    @Test
    void shouldCheckAndScheduleTasks_whenFutureMessagesExist() {
        given(clock.getZone()).willReturn(ZONED_DATE_TIME.getZone());

        List<NewsletterMessage> futureMessages = Collections.singletonList(newsletterMessage);
        given(newsletterMessageService.getAllFutureMessages()).willReturn(futureMessages);
        given(taskScheduler.schedule(any(Runnable.class), any(Instant.class))).willReturn(mock(ScheduledFuture.class));

        underTest.checkAndScheduleTasks();

        verify(newsletterMessageService, times(1)).getAllFutureMessages();
        verify(taskScheduler, times(1)).schedule(any(Runnable.class), eq(newsletterMessage.getSendDate().atZone(ZoneId.systemDefault()).toInstant()));
        verifyNoMoreInteractions(notifyNewsletterSubscriberService);

    }

    @Test
    void shouldCheckAndScheduleTasks_whenFutureMessagesExistAndExecuteTask() {
        given(clock.getZone()).willReturn(ZONED_DATE_TIME.getZone());

        List<NewsletterMessage> futureMessages = Collections.singletonList(newsletterMessage);
        given(newsletterMessageService.getAllFutureMessages()).willReturn(futureMessages);
        given(taskScheduler.schedule(any(Runnable.class), any(Instant.class))).willAnswer(invocation -> {
            Runnable task = invocation.getArgument(0);
            task.run();
            return mock(ScheduledFuture.class);
        });

        underTest.checkAndScheduleTasks();

        verify(newsletterMessageService, times(1)).getAllFutureMessages();
        verify(taskScheduler, times(1)).schedule(any(Runnable.class), eq(newsletterMessage.getSendDate().atZone(ZoneId.systemDefault()).toInstant()));
        verify(notifyNewsletterSubscriberService, times(1)).notifyEnabledSubscribers(newsletterMessage.getMessage());
    }
}
