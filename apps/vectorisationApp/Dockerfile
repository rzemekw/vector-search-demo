FROM python:3.10.6

WORKDIR /code

RUN apt-get update && apt-get install -y protobuf-compiler

COPY ./requirements.txt /code/requirements.txt
RUN pip install --no-cache-dir --upgrade -r /code/requirements.txt

COPY ./app /code/

CMD ["uvicorn", "server:app", "--host", "0.0.0.0", "--port", "80"]
