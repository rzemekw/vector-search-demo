import {Component, OnInit} from '@angular/core';
import {ProductService} from "../../service/product.service";
import {SearchType} from "../../models/product.model";
import {MatSelectChange} from "@angular/material/select";

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.scss']
})
export class NavBarComponent implements OnInit {

  searchType = SearchType

  selectedSearchType: SearchType = SearchType.DEFAULT;
  constructor(private productService: ProductService) {
  }

  searchTypeChanged($event: MatSelectChange) {
    this.productService.setSearchType($event.value)
  }

  ngOnInit(): void {
    this.selectedSearchType = this.productService.getSearchType()
  }
}


