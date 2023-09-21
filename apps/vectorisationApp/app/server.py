import asyncio
from dataclasses import dataclass
from io import BytesIO
from fastapi import FastAPI, File, Response, UploadFile
from PIL import Image
from fastapi.responses import HTMLResponse
import torch
import open_clip

model, _, preprocess = open_clip.create_model_and_transforms('xlm-roberta-large-ViT-H-14', pretrained='frozen_laion5b_s13b_b90k')
tokenizer = open_clip.get_tokenizer('xlm-roberta-large-ViT-H-14')

@dataclass
class TextsHolder:
  texts: list[str]

async def process_image_files(file: list[UploadFile]):
  contents = await asyncio.gather(*[f.read() for f in file])
  images = [Image.open(BytesIO(content)) for content in contents]
  preprocessed_images = [preprocess(image) for image in images]
  image_tensor = torch.stack(preprocessed_images)

  with torch.no_grad(), torch.cuda.amp.autocast():
    vector = model.encode_image(image_tensor)
    vector /= vector.norm(dim=-1, keepdim=True)
    return vector.tolist()

def process_text(texts: list[str]):
  tokenized = tokenizer(texts)
  with torch.no_grad(), torch.cuda.amp.autocast():
    vector = model.encode_text(tokenized)
    vector /= vector.norm(dim=-1, keepdim=True)
    return vector.tolist()


app = FastAPI()

@app.post("/images")
async def main_endpoint(
  files: list[UploadFile] = File(...),
):
  return await process_image_files(files)

@app.post("/texts")
async def main(nody: TextsHolder):
    return process_text(nody.texts)

@app.get("/")
async def main():
    content = """
<body>
<form action="/images" enctype="multipart/form-data" method="post">
<input name="files" type="file" multiple>
<input type="submit">
</form>
<form action="/uploadfiles/" enctype="multipart/form-data" method="post">
<input name="files" type="file" multiple>
<input type="submit">
</form>
</body>
    """
    return HTMLResponse(content=content)
