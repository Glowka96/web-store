import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { Product } from 'src/app/models/product';
import { Shipment } from 'src/app/models/shipment';
import { ProductService } from 'src/app/services/product.service';
import { ShopService } from 'src/app/services/shop.service';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss'],
})
export class ProductsComponent implements OnInit {
  private products: Product[] = [];
  private cart: { [productId: string]: number } = {};
  private title!: string;
  private quantityOfProducts!: number;
  private subcategoryId!: string;
  private countPage!: number;
  private searchQuery!: string;
  private pageClicked: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private shopService: ShopService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      if (params.get('id')) {
        this.subcategoryId = params.get('id') as string;
        this.getProductsBySubcategoryId(this.subcategoryId);
        this.getQuantityOfProductsBySubcategoryId(this.subcategoryId);
        this.title = params.get('subcategoryName') as string;
      }
    });
    this.route.queryParams.subscribe((params: Params) => {
      if (params['q'] && !this.pageClicked) {
        this.searchQuery = params['q'];
        this.getSearchProducts(this.searchQuery);
        this.getQuanititySearchProducts(this.searchQuery);
        this.title = 'Result of search';
      }
      this.pageClicked = false;
    });
  }

  private getProductsBySubcategoryId(subcategoryId: string) {
    this.productService
      .getProductsBySubcategory(subcategoryId)
      .subscribe((products) => {
        products.forEach((product) => (this.cart[product.id] = 1));
        this.products = products;
      });
  }

  private getQuantityOfProductsBySubcategoryId(subcategoryId: string) {
    this.productService
      .getQuantityOfProducts(subcategoryId)
      .subscribe((value) => {
        this.quantityOfProducts = value;
        this.countPage = Math.ceil(value / 12);
      });
  }

  private getSearchProducts(text: string) {
    this.productService.getSearchProducts(text).subscribe((products) => {
      console.log('start get products');
      products.forEach((product) => (this.cart[product.id] = 1));
      this.products = products;
    });
  }

  private getQuanititySearchProducts(text: string) {
    this.productService.getQuanititySearchProducts(text).subscribe((value) => {
      console.log('start get amount products');
      this.quantityOfProducts = value;
      this.countPage = Math.ceil(value / 12);
    });
  }

  public getPageProducts(page: number) {
    this.pageClicked = true;
    if (this.title.match('search')) {
      this.productService
        .getSearchProducts(this.searchQuery, page)
        .subscribe((products) => {
          products.forEach((product) => (this.cart[product.id] = 1));
          this.products = products;
        });
    } else {
      this.productService
        .getProductsBySubcategory(this.subcategoryId, page)
        .subscribe((products) => {
          products.forEach((product) => (this.cart[product.id] = 1));
          this.products = products;
        });
    }
  }

  public getPrice(product: Product): string {
    return (parseFloat(product.price) * this.cart[product.id]).toFixed(2);
  }

  public addToBasket(id: string) {
    let product = this.products.find((product: Product) => {
      return product.id == id;
    });
    if (product) {
      let price = Number(this.getPrice(product));
      console.log('price: ' + price);

      let quality = this.cart[product.id];
      let shipment = { product, quality, price };
      this.shopService.addToBasket(shipment);
      console.log('shipment price: ' + shipment.price);
    }
  }

  getProductQuantity(productId: string) {
    console.log('cart ' + this.cart[productId]);
    return this.cart[productId] || 1;
  }

  increaseProductQuantity(productId: string) {
    if (this.cart[productId]) {
      this.cart[productId] += 1;
      console.log(this.cart);
    }
  }

  decreaseProductQuantity(productId: string) {
    if (this.cart[productId] > 1) {
      this.cart[productId] -= 1;
    }
  }

  public get listProduct(): Product[] {
    return this.products;
  }

  public get getTitle(): string {
    return this.title.toUpperCase();
  }

  public get quantityOfProduct(): number {
    return this.quantityOfProducts;
  }

  public get countPagesArray(): number[] {
    return Array.from({ length: this.countPage }, (_, i) => i);
  }
}