import {Component, OnInit, RendererFactory2} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {ProductService} from "../../service/product.service";
import {lastValueFrom} from "rxjs";
import {DEFAULT_SRC} from "../../constants/image.constants";

@Component({
  selector: 'app-product-form',
  templateUrl: './product-form.component.html',
  styleUrls: ['./product-form.component.scss']
})
export class ProductFormComponent implements OnInit {
  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private rendererFactory: RendererFactory2,
    private router: Router,
  ) {
  }

  loading = true;

  productId: string | null = null;

  description: string | null = "";
  selectedFile: File | null = null;
  imageUrl: string | null = null;

  private fileInput!: HTMLInputElement;

  src = DEFAULT_SRC;

  ngOnInit(): void {
    this.initFileInput();

    this.route.paramMap.subscribe(p => {
      this.loading = true;
      this.productId = p.get('productId');
      this.description = null;
      this.selectedFile = null;
      this.src = DEFAULT_SRC;
      this.imageUrl = null;

      if (this.productId) {
        this.productService.getProduct(+this.productId).subscribe(p => {
          this.description = p.description;
          this.src = p.imageUrl ?? DEFAULT_SRC;
          this.imageUrl = p.imageUrl;
          this.loading = false;
        })
      } else {
        this.loading = false;
      }
    })
  }


  private initFileInput() {
    const renderer = this.rendererFactory.createRenderer(null, null);
    this.fileInput = renderer.createElement('input') as HTMLInputElement;
    this.fileInput.type = 'file';
    this.fileInput.style.display = 'none';
    this.fileInput.addEventListener('change', () => this.onFilesDropped(this.fileInput.files));
    renderer.appendChild(document.body, this.fileInput);
  }

  private onFilesDropped($event: FileList | null) {
    if ($event?.length) {
      this.selectedFile = $event[0];
      const reader = new FileReader();
      reader.onload = e => {
        this.src = e.target?.result as string;
      }
      reader.readAsDataURL(this.selectedFile);
    }
  }

  onImageClick() {
    this.fileInput.click();
  }

  protected readonly onsubmit = onsubmit;

  async onSubmit() {
    if (!this.selectedFile && !this.description && !this.imageUrl) {
      return;
    }
    if (this.productId) {
      const body = {
        description: this.description,
        id: +this.productId,
        image: this.selectedFile,
      }
      const result = await lastValueFrom(this.productService.updateProduct(body));
      await this.router.navigate(['/products', result.id]);
      return;

    }

    const body = {
      description: this.description,
      image: this.selectedFile,
    }
    const result = await lastValueFrom(this.productService.createProduct(body));
    await this.router.navigate(['/products', result.id]);
  }
}


