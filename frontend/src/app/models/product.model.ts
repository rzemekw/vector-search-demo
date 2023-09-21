export interface ProductModel {
  id: number;
  description: string;
  imageUrl: string | null;
}

export interface ProductDTO {
  id: number;
  description: string;
  imageId: string;
}

export interface SaveProductModel {
  id?: number | null;
  description: string | null;
  image: File | null;
}

export enum SearchType {
  DEFAULT = "DEFAULT",
  TEXT = "TEXT",
  IMAGE = "IMAGE"
}