import {Component, Input, OnInit} from '@angular/core';
import {DEFAULT_SRC} from "../../constants/image.constants";
import {ProductModel} from "../../models/product.model";

@Component({
  selector: 'app-product-item',
  templateUrl: './product-item.component.html',
  styleUrls: ['./product-item.component.scss']
})
export class ProductItemComponent implements OnInit {

  @Input()
  product!: ProductModel;

  src: string = DEFAULT_SRC;

  ngOnInit() {
    if (this.product.imageUrl) {
      this.src = this.product.imageUrl;
    }
  }
}


