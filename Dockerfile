# Should add amazon-corretto-17 to image
FROM alpine:3.17
    RUN apk add --no-cache &&\
            wget -O /etc/apk/keys/amazoncorretto.rsa.pub https://apk.corretto.aws/amazoncorretto.rsa.pub && \
            echo "https://apk.corretto.aws" >> /etc/apk/repositories && \
            apk update &&\
            apk add amazon-corretto-17