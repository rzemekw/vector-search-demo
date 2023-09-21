import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {ProductDTO, ProductModel, SaveProductModel, SearchType} from "../models/product.model";
import {map} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  constructor(private http: HttpClient) {
  }

  private searchType!: SearchType;

  getProduct(id: number) {
    return this.http.get<ProductDTO>(`/api/products/${id}`).pipe(
      map(productDTO => this.mapToProductModel(productDTO))
    )
  }

  searchProducts(text?: string) {
    const body = {
      text: text,
      byImage: this.getSearchType() === SearchType.IMAGE || this.getSearchType() === SearchType.DEFAULT,
      byDescription: this.getSearchType() === SearchType.TEXT || this.getSearchType() === SearchType.DEFAULT
    }
    return this.http.post<ProductDTO[]>('/api/products/search/text', body).pipe(
      map((p) => p.map(a => this.mapToProductModel(a)))
    );
  }

  searchProductsByProduct(productId: number) {
    const body = {
      productId: productId,
      byImage: this.getSearchType() === SearchType.IMAGE || this.getSearchType() === SearchType.DEFAULT,
      byDescription: this.getSearchType() === SearchType.TEXT || this.getSearchType() === SearchType.DEFAULT
    }
    return this.http.post<ProductDTO[]>('/api/products/search/product', body).pipe(
      map((p) => p.map(a => this.mapToProductModel(a)))
    );
  }

  createProduct(product: SaveProductModel) {
    const formData = new FormData();
    formData.append('data', JSON.stringify(this.mapToSaveProductDataDTO(product)));
    if (product.image) {
      formData.append('file', product.image);
    }

    return this.http.post<ProductDTO>('/api/products', formData).pipe(
      map(productDTO => this.mapToProductModel(productDTO))
    );
  }

  updateProduct(product: SaveProductModel) {
    const formData = new FormData();
    formData.append('data', JSON.stringify(this.mapToSaveProductDataDTO(product)));
    if (product.image) {
      formData.append('file', product.image);
    }

    return this.http.put<ProductDTO>('/api/products', formData).pipe(
      map(productDTO => this.mapToProductModel(productDTO))
    );
  }

  private mapToSaveProductDataDTO(product: SaveProductModel) {
    return {
      description: product.description,
      id: product.id
    }
  }

  private mapToProductModel(productDTO: ProductDTO): ProductModel {
    return {
      id: productDTO.id,
      description: productDTO.description,
      imageUrl: productDTO.imageId ? `/api/images/${productDTO.imageId}` : null
    }
  }

  getSearchType() {
    if (this.searchType) {
      return this.searchType;
    }
    const prev = localStorage.getItem('searchType');
    if (prev) {
      this.searchType = prev as SearchType;
      return this.searchType;
    }
    this.searchType = SearchType.DEFAULT
    return this.searchType;
  }

  setSearchType(value: SearchType) {
    this.searchType = value;
    localStorage.setItem('searchType', value);
  }
}