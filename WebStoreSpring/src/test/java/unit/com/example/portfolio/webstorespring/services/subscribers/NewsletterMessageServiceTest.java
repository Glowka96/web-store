package com.example.portfolio.webstorespring.services.subscribers;

import com.example.portfolio.webstorespring.models.dtos.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.dtos.subscribers.NewsletterMessageRequest;
import com.example.portfolio.webstorespring.models.entities.subscribers.NewsletterMessage;
import com.example.portfolio.webstorespring.repositories.subscribers.NewsletterMessageRepository;
import com.example.portfolio.webstorespring.services.emails.notifications.NotifyNewsletterSubscriberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static com.example.portfolio.webstorespring.buildhelpers.DateForTestBuilderHelper.LOCAL_DATE_TIME;
import static com.example.portfolio.webstorespring.buildhelpers.subscribers.NewsletterMessageBuilderHelper.createNewsletterMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class NewsletterMessageServiceTest {

    @Mock
    private NewsletterMessageRepository repository;
    @Mock
    private NotifyNewsletterSubscriberService notifyService;
    @InjectMocks
    private NewsletterMessageService underTest;

    @Test
    void shouldGetAllFutureMessages() {
        NewsletterMessage message = createNewsletterMessage();
        NewsletterMessage message2 = createNewsletterMessage();
        given(repository.findFutureMessages()).willReturn(List.of(message, message2));

        List<NewsletterMessage> result = underTest.getAllFutureMessages();

        assertEquals(List.of(message, message2), result);
        verify(repository, times(1)).findFutureMessages();
    }

    @ParameterizedTest
    @MethodSource("providerForSave")
    void shouldSave(NewsletterMessageRequest newsletterMessageRequest, String exceptedResponseMessage) {
        ResponseMessageDTO result = underTest.save(newsletterMessageRequest);

        assertNotNull(result);
        assertEquals(exceptedResponseMessage, result.message());
    }


    private Stream<Arguments> providerForSave() {
        return Stream.of(
                Arguments.of(
                        new NewsletterMessageRequest("Example test", null),
                        "Newsletter message send to enabled subscribers"
                ),
                Arguments.of(
                        new NewsletterMessageRequest("Example test", LOCAL_DATE_TIME),
                        String.format("Newsletter message will be send at the date: %s", LOCAL_DATE_TIME)
                )
        );
    }
}