import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ShopService } from './services/shop.service';
import { ContentComponent } from './components/content/content.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AuthHttpInterceptorService } from './services/auth-http-interceptor.service';
import { NavigationComponent } from './components/navigation/navigation.component';
import { ProductsComponent } from './components/products/products.component';
import { FilterComponent } from './components/filter/filter.component';
import { FormComponent } from './components/form/form.component';
import { ModCategoryComponent } from './components/mod-category/mod-category.component';
import { BoardAdminComponent } from './components/board-admin/board-admin.component';
import { ModSubcategoryComponent } from './components/mod-subcategory/mod-subcategory.component';
import { ModProductsComponent } from './components/mod-products/mod-products.component';
import { ModProducerComponent } from './components/mod-producer/mod-producer.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AccountComponent } from './components/account/account.component';

@NgModule({
  declarations: [
    AppComponent,
    NavigationComponent,
    ProductsComponent,
    FilterComponent,
    ContentComponent,
    FormComponent,
    ModCategoryComponent,
    BoardAdminComponent,
    ModSubcategoryComponent,
    ModProductsComponent,
    ModProducerComponent,
    AccountComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule,
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
