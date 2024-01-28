import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BoardAdminComponent } from './components/admin/board-admin/board-admin.component';
import { AccountComponent } from './components/user/account/account.component';
import { BasketComponent } from './components/basket/basket.component';
import { PurchaseComponent } from './components/purchase/purchase.component';
import { FormAccountComponent } from './components/forms/form-account/form-account.component';
import { FormAccountAddressComponent } from './components/forms/form-account-address/form-account-address.component';
import { OrdersComponent } from './components/user/orders/orders.component';
import { HomeComponent } from './components/home/home.component';
import { FormResetPasswordComponent } from './components/forms/form-reset-password/form-reset-password.component';
import { ConfirmAccountComponent } from './components/confirms/confirm-account/confirm-account.component';
import { ConfirmResetPasswordComponent } from './components/confirms/confirm-reset-password/confirm-reset-password.component';
import { PageBySubcategoryProducts } from './components/page-products/by-subcategory-products/page-by-subcategory-products.component';

const routes: Routes = [
  {
    path: ':categoryName/:subcategoryName/:id/products',
    component: PageBySubcategoryProducts,
  },
  { path: 'admin-board', component: BoardAdminComponent },
  { path: 'accounts', component: AccountComponent },
  { path: 'basket', component: BasketComponent },
  { path: 'basket/purchase', component: PurchaseComponent },
  { path: 'accounts/update', component: FormAccountComponent },
  { path: 'accounts/address/update', component: FormAccountAddressComponent },
  { path: 'accounts/orders', component: OrdersComponent },
  { path: '', component: HomeComponent },
  { path: 'registration/confirm', component: ConfirmAccountComponent },
  { path: 'reset-password', component: FormResetPasswordComponent },
  { path: 'reset-password/confirm', component: ConfirmResetPasswordComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
