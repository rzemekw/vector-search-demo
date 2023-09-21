import torch
import numpy as np
from scipy.spatial import distance

# Load tokenizer and model
import open_clip

model, _, preprocess = open_clip.create_model_and_transforms('xlm-roberta-large-ViT-H-14', pretrained='frozen_laion5b_s13b_b90k')
tokenizer = open_clip.get_tokenizer('xlm-roberta-large-ViT-H-14')

def process_text(text: str):
  tokenized = tokenizer([text])
  with torch.no_grad(), torch.cuda.amp.autocast():
    vector = model.encode_text(tokenized)
    vector /= vector.norm(dim=-1, keepdim=True)
    return vector.numpy()[0]



def cosine_similarity(vec1, vec2):
    return 1 - distance.cosine(vec1, vec2)
    dot_product = np.dot(vec1, vec2)
    norm_vec1 = np.linalg.norm(vec1)
    norm_vec2 = np.linalg.norm(vec2)

    similarity = dot_product / (norm_vec1 * norm_vec2)
    return np.clip(similarity, -1.0, 1.0)  # Ensure value is between -1 and 1

# Example
text1 = "zwierzęta"
text2 = "meble dąb sodoma"
text3 = "Sprzedam krowy 20sztuk dojnych. Więcej informacji pod numerem telefonu"
vector1 = process_text(text1)
vector2 = process_text(text2)
vector3 = process_text(text3)

print(cosine_similarity(vector1, vector2))
print(cosine_similarity(vector1, vector3))