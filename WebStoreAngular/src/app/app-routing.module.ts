import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BoardAdminComponent } from './components/board-admin/board-admin.component';
import { ProductsComponent } from './components/products/products.component';
import { AccountComponent } from './components/account/account.component';
import { BasketComponent } from './components/basket/basket.component';
import { PurchaseComponent } from './components/purchase/purchase.component';
import { FormAccountComponent } from './components/form-account/form-account.component';
import { FormAccountAddressComponent } from './components/form-account-address/form-account-address.component';
import { OrdersComponent } from './components/orders/orders.component';
import { HomeComponent } from './components/home/home.component';
import { ConfirmComponent } from './components/confirm/confirm.component';

const routes: Routes = [
  {
    path: ':categoryName/:subcategoryName/:id/products',
    component: ProductsComponent,
  },
  { path: 'search', component: ProductsComponent },
  { path: 'admin-board', component: BoardAdminComponent },
  { path: 'accounts', component: AccountComponent },
  { path: 'basket', component: BasketComponent },
  { path: 'basket/purchase', component: PurchaseComponent },
  { path: 'accounts/update', component: FormAccountComponent },
  { path: 'accounts/address/update', component: FormAccountAddressComponent },
  { path: 'accounts/orders', component: OrdersComponent },
  { path: '', component: HomeComponent },
  { path: 'registration/confirm', component: ConfirmComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
