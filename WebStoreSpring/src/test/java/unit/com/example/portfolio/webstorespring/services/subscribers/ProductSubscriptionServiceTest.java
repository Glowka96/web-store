package com.example.portfolio.webstorespring.services.subscribers;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.models.dto.products.ProductNameView;
import com.example.portfolio.webstorespring.models.entity.products.Product;
import com.example.portfolio.webstorespring.models.entity.subscribers.ProductSubscriber;
import com.example.portfolio.webstorespring.models.entity.subscribers.ProductSubscription;
import com.example.portfolio.webstorespring.repositories.subscribers.ProductSubscriptionRepository;
import com.example.portfolio.webstorespring.services.products.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.subscribers.ProductSubscriberBuilderHelper.BASIC_PRODUCT_SUBSCRIBER;
import static com.example.portfolio.webstorespring.buildhelpers.subscribers.ProductSubscriptionBuilderHelper.createProductSubscription;
import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductSubscriptionServiceTest {

    @Mock
    private ProductSubscriptionRepository productSubscriptionRepository;
    @Mock
    private ProductService productService;
    @InjectMocks
    private ProductSubscriptionService underTest;


    @Test
    void shouldGetWithEnabledSubscribersByProductId() {
        ProductSubscription productSubscription = createProductSubscription();

        given(productSubscriptionRepository.findByIdWithEnabledSubscribers(anyLong())).willReturn(Optional.of(productSubscription));

        ProductSubscription result = underTest.getWithEnabledSubscribersByProductId(1L);

        assertEquals(productSubscription, result);
    }

    @Test
    void willThrowResourceNotFound_whenNotExistProductSubscriptionWithEnabledSubscribers() {
        given(productSubscriptionRepository.findByIdWithEnabledSubscribers(anyLong())).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> underTest.getWithEnabledSubscribersByProductId(anyLong()));
    }

    @Test
    void shouldAddSubscriberAndCreateNewSubscription() {
        ProductSubscriber productSubscriber = make(a(BASIC_PRODUCT_SUBSCRIBER));
        ProductSubscription productSubscription = createProductSubscription();
        productSubscription.addSubscriber(productSubscriber);
        Product product = productSubscription.getProduct();

        given(productSubscriptionRepository.findById(anyLong())).willReturn(Optional.empty());
        given(productService.findById(anyLong())).willReturn(product);

        underTest.add(productSubscriber, 1L);

        ArgumentCaptor<ProductSubscription> subscriptionArgumentCaptor =
                ArgumentCaptor.forClass(ProductSubscription.class);
        verify(productSubscriptionRepository, times(1)).save(subscriptionArgumentCaptor.capture());

        assertEquals(productSubscription, subscriptionArgumentCaptor.getValue());
        assertTrue(subscriptionArgumentCaptor.getValue().getProductSubscribers().contains(productSubscriber));
    }

    @Test
    void shouldAddSubscriberAndFetchExistedSubscription() {
        ProductSubscriber productSubscriber = make(a(BASIC_PRODUCT_SUBSCRIBER));
        ProductSubscription productSubscription = createProductSubscription();
        productSubscription.addSubscriber(productSubscriber);

        given(productSubscriptionRepository.findById(anyLong())).willReturn(Optional.of(productSubscription));

        underTest.add(productSubscriber, 1L);

        ArgumentCaptor<ProductSubscription> subscriptionArgumentCaptor =
                ArgumentCaptor.forClass(ProductSubscription.class);
        verify(productSubscriptionRepository, times(1)).save(subscriptionArgumentCaptor.capture());

        assertEquals(productSubscription, subscriptionArgumentCaptor.getValue());
        assertTrue(subscriptionArgumentCaptor.getValue().getProductSubscribers().contains(productSubscriber));
    }

    @Test
    void shouldRemoveForSingleProduct() {
        ProductSubscriber productSubscriber = make(a(BASIC_PRODUCT_SUBSCRIBER));
        ProductSubscription productSubscription = createProductSubscription();
        productSubscription.addSubscriber(productSubscriber);

        ProductNameView productNameView = mock(ProductNameView.class);
        given(productService.getNameById(anyLong())).willReturn(productNameView);
        given(productNameView.getName()).willReturn("Test Product");

        given(productSubscriptionRepository.findByIdAndSubscriberEmail(anyLong(), anyString())).willReturn(Optional.of(productSubscription));

        underTest.removeForSingleProduct(productSubscriber, 1L);

        ArgumentCaptor<ProductSubscription> subscriptionArgumentCaptor =
                ArgumentCaptor.forClass(ProductSubscription.class);
        verify(productSubscriptionRepository, times(1)).save(subscriptionArgumentCaptor.capture());

        assertEquals(productSubscription, subscriptionArgumentCaptor.getValue());
        assertFalse(subscriptionArgumentCaptor.getValue().getProductSubscribers().contains(productSubscriber));
    }

    @Test
    void willThrowResourceNotFound_whenNotFoundByIdAndSubscriberEmail() {
        ProductSubscriber productSubscriber = make(a(BASIC_PRODUCT_SUBSCRIBER));

        given(productSubscriptionRepository.findByIdAndSubscriberEmail(anyLong(), anyString())).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, ()-> underTest.removeForSingleProduct(productSubscriber, 1L));
    }

    @Test
    void shouldDeleteById() {
        underTest.deleteById(anyLong());

        verify(productSubscriptionRepository, times(1)).deleteById(anyLong());
    }
}