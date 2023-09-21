import torch
import torch.nn as nn
from torch.utils.data import Dataset, DataLoader
from PIL import Image
from torch.optim import Adam
import open_clip

# Constants
BATCH_SIZE = 4
EPOCH = 10

device = "cuda:0" if torch.cuda.is_available() else "cpu"

model, _, preprocess = open_clip.create_model_and_transforms('xlm-roberta-large-ViT-H-14', pretrained='frozen_laion5b_s13b_b90k')
tokenizer = open_clip.get_tokenizer('xlm-roberta-large-ViT-H-14')

class ImageTitleDataset(Dataset):
    def __init__(self, list_image_path, list_txt):
        self.image_path = list_image_path
        self.title = tokenizer(list_txt).to(device)

    def __len__(self):
        return len(self.title)

    def __getitem__(self, idx):
        image = preprocess(Image.open(self.image_path[idx]))
        title = self.title[idx]
        return image, title

# Use your own data
list_image_path = ['folder/image1.jpg', 'folder2/image2.jpg']
list_txt = ['description for image1.jpg', 'description for image2.jpg']

dataset = ImageTitleDataset(list_image_path, list_txt)
train_dataloader = DataLoader(dataset, batch_size=BATCH_SIZE, shuffle=True)

loss_fn = nn.CrossEntropyLoss()
optimizer = Adam(model.parameters(), lr=5e-5, betas=(0.9, 0.98), eps=1e-6, weight_decay=0.2)

for epoch in range(EPOCH):
    for batch in train_dataloader:
        optimizer.zero_grad()

        images, texts = batch
        images = images.to(device)
        texts = texts.to(device)

        with torch.no_grad(), torch.cuda.amp.autocast():
            logits_per_image = model.encode_image(images)
            logits_per_text = model.encode_text(texts)

        ground_truth = torch.arange(len(images), dtype=torch.long, device=device)
        total_loss = (loss_fn(logits_per_image, ground_truth) + loss_fn(logits_per_text, ground_truth)) / 2
        total_loss.backward()

        optimizer.step()
