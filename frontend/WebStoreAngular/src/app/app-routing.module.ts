import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ContentComponent } from './components/content/content.component';
import { BoardAdminComponent } from './components/board-admin/board-admin.component';
import { ProductsComponent } from './components/products/products.component';

const routes: Routes = [
  { path: ':subcategoryName/:id/products', component: ProductsComponent },
  { path: 'search', component: ContentComponent },
  { path: 'admin-board', component: BoardAdminComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
