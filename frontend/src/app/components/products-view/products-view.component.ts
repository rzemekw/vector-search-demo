import {Component, OnInit} from '@angular/core';
import {ProductService} from "../../service/product.service";
import {ProductModel} from "../../models/product.model";

@Component({
  selector: 'app-products-view',
  templateUrl: './products-view.component.html',
  styleUrls: ['./products-view.component.scss']
})
export class ProductsViewComponent implements OnInit {

  constructor(
    private productService: ProductService,
  ) {
  }

  products: ProductModel[] = [];
  text = ""


  ngOnInit(): void {
    this.productService.searchProducts()
      .subscribe(p => this.products = p)
  }

  search() {
    this.productService.searchProducts(this.text ? this.text : undefined)
      .subscribe(p => this.products = p)
  }
}


