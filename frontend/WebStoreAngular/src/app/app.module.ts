import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavigationComponent } from './navigation/navigation.component';
import { ProductsComponent } from './products/products.component';
import { ShopService } from './shop.service';

@NgModule({
  declarations: [AppComponent, NavigationComponent, ProductsComponent],
  imports: [BrowserModule, AppRoutingModule, HttpClientModule],
  providers: [ShopService],
  bootstrap: [AppComponent],
})
export class AppModule {}
