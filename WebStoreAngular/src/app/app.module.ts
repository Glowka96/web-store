import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ShopService } from './services/olders/shop.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NavigationComponent } from './components/navigation/navigation.component';
import { ModCategoryComponent } from './components/admin/modifications/mod-category/mod-category.component';
import { BoardAdminComponent } from './components/admin/board-admin/board-admin.component';
import { ModSubcategoryComponent } from './components/admin/modifications/mod-subcategory/mod-subcategory.component';
import { ModProductsComponent } from './components/admin/modifications/mod-products/mod-products.component';
import { ModProducerComponent } from './components/admin/modifications/mod-producer/mod-producer.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AccountComponent } from './components/user/account/account.component';
import { BasketComponent } from './components/basket/basket.component';
import { FooterComponent } from './components/footer/footer.component';
import { PurchaseComponent } from './components/purchase/purchase.component';
import { FormLoginComponent } from './components/user/forms/form-login/form-login.component';
import { FormAccountComponent } from './components/user/forms/form-account/form-account.component';
import { FormAccountAddressComponent } from './components/user/forms/form-account-address/form-account-address.component';
import { OrdersComponent } from './components/user/orders/orders.component';
import { LayoutModule } from '@angular/cdk/layout';
import { HomeComponent } from './components/home/home.component';
import { FormResetPasswordComponent } from './components/user/forms/form-reset-password/form-reset-password.component';
import { AuthHttpInterceptorService } from './services/accounts/auth-http-interceptor.service';
import { ConfirmAccountComponent } from './components/user/confirms/confirm-account/confirm-account.component';
import { ConfirmResetPasswordComponent } from './components/user/confirms/confirm-reset-password/confirm-reset-password.component';
import { PageBySubcategoryId } from './components/page-products/page-by-subcategory-id/page-by-subcategory-id.component';
import { PageNewProductsComponent } from './components/page-products/page-new-products/page-new-products.component';
import { PagePromotionsProductsComponent } from './components/page-products/page-promotion-products/page-promotion-products.component';
import { PageBySearchTextComponent } from './components/page-products/page-by-search-text/page-by-search-text.component';
import { ModProductTypeComponent } from './components/admin/modifications/mod-product-type/mod-product-type.component';
import { ModDeliveryTypeComponent } from './components/admin/modifications/mod-delivery-type/mod-delivery-type.component';
import { ProductComponent } from './components/product/product.component';
import { ModPromotionComponent } from './components/admin/modifications/mod-promotion/mod-promotion.component';
import { OrderComponent } from './components/user/order/order.component';

@NgModule({
  declarations: [
    AppComponent,
    NavigationComponent,
    FormLoginComponent,
    ModCategoryComponent,
    BoardAdminComponent,
    ModSubcategoryComponent,
    ModProductsComponent,
    ModProducerComponent,
    AccountComponent,
    BasketComponent,
    FooterComponent,
    PurchaseComponent,
    FormAccountComponent,
    FormAccountAddressComponent,
    OrdersComponent,
    HomeComponent,
    ConfirmAccountComponent,
    ConfirmResetPasswordComponent,
    FormResetPasswordComponent,
    PageBySubcategoryId,
    PageNewProductsComponent,
    PagePromotionsProductsComponent,
    PageBySearchTextComponent,
    ModProductTypeComponent,
    ModDeliveryTypeComponent,
    ProductComponent,
    ModPromotionComponent,
    OrderComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule,
    LayoutModule,
  ],
  providers: [
    ShopService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthHttpInterceptorService,
      multi: true,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
