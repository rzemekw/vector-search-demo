import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {ProductService} from "../../service/product.service";
import {DEFAULT_SRC} from "../../constants/image.constants";
import {ProductModel} from "../../models/product.model";

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.scss']
})
export class ProductDetailsComponent implements OnInit {
  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private router: Router,
  ) {
  }

  src: string = DEFAULT_SRC;
  description: string | null = null;
  loading = true;
  productId!: number;

  relatedProducts: ProductModel[] = [];

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.loading = true;
      const productId = params.get('productId') as string;
      this.productId = +productId;
      this.productService.getProduct(this.productId).subscribe(p => {
        this.description = p.description;
        this.src = p.imageUrl ?? DEFAULT_SRC;
        this.loading = false;
      })
      this.productService.searchProductsByProduct(this.productId).subscribe(p => {
        this.relatedProducts = p;
      });
    });
  }

  editClicked() {
    this.router.navigate(['edit-product', this.productId]);
  }
}


