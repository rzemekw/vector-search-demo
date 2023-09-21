import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {RouterModule} from '@angular/router';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

import {AppComponent} from './components/app.component';
import {ProductsViewComponent} from './components/products-view/products-view.component';
import {ProductDetailsComponent} from "./components/product-details/product-details.component";
import {NavBarComponent} from "./components/nav-bar/nav-bar.component";
import {MatSelectModule} from "@angular/material/select";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {ProductFormComponent} from "./components/product-form/product-form.component";
import {HttpClientModule} from "@angular/common/http";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {ProductItemComponent} from "./components/product-item/product-item.component";
import {MatCardModule} from "@angular/material/card";
import {MatInputModule} from "@angular/material/input";
import {ProductListComponent} from "./components/product-list/product-list.component";

@NgModule({
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    RouterModule.forRoot([
      {path: '', component: ProductsViewComponent},
      {path: 'products/:productId', component: ProductDetailsComponent},
      {path: 'create-product', component: ProductFormComponent},
      {path: 'edit-product/:productId', component: ProductFormComponent},
    ]),
    MatSelectModule,
    BrowserAnimationsModule,
    HttpClientModule,
    MatButtonModule,
    FormsModule,
    MatIconModule,
    MatCardModule,
    MatInputModule
  ],
  declarations: [
    AppComponent,
    ProductsViewComponent,
    ProductDetailsComponent,
    NavBarComponent,
    ProductFormComponent,
    ProductItemComponent,
    ProductListComponent,
  ],
  bootstrap: [
    AppComponent
  ],
  providers: [
  ]
})
export class AppModule {
}


