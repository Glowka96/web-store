import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BoardAdminComponent } from './components/board-admin/board-admin.component';
import { ProductsComponent } from './components/products/products.component';

const routes: Routes = [
  { path: ':subcategoryName/:id/products', component: ProductsComponent },
  { path: 'search', component: ProductsComponent },
  { path: 'admin-board', component: BoardAdminComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
