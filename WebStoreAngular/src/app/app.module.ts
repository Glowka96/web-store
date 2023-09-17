import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ShopService } from './services/shop.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NavigationComponent } from './components/navigation/navigation.component';
import { ProductsComponent } from './components/products/products.component';
import { ModCategoryComponent } from './components/admin/modification/mod-category/mod-category.component';
import { BoardAdminComponent } from './components/admin/board-admin/board-admin.component';
import { ModSubcategoryComponent } from './components/admin/modification/mod-subcategory/mod-subcategory.component';
import { ModProductsComponent } from './components/admin/modification/mod-products/mod-products.component';
import { ModProducerComponent } from './components/admin/modification/mod-producer/mod-producer.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AccountComponent } from './components/user/account/account.component';
import { BasketComponent } from './components/basket/basket.component';
import { FooterComponent } from './components/footer/footer.component';
import { PurchaseComponent } from './components/purchase/purchase.component';
import { FormLoginComponent } from './components/forms/form-login/form-login.component';
import { FormAccountComponent } from './components/forms/form-account/form-account.component';
import { FormAccountAddressComponent } from './components/forms/form-account-address/form-account-address.component';
import { OrdersComponent } from './components/user/orders/orders.component';
import { LayoutModule } from '@angular/cdk/layout';
import { HomeComponent } from './components/home/home.component';
import { FormResetPasswordComponent } from './components/forms/form-reset-password/form-reset-password.component';
import { AuthHttpInterceptorService } from './services/accounts/auth-http-interceptor.service';
import { ConfirmAccountComponent } from './components/confirms/confirm-account/confirm-account.component';
import { ConfirmResetPasswordComponent } from './components/confirms/confirm-reset-password/confirm-reset-password.component';

@NgModule({
  declarations: [
    AppComponent,
    NavigationComponent,
    ProductsComponent,
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
