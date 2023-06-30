# STEP 1 build static website
FROM node:alpine as builder
RUN apk update && apk add --no-cache make git
# Create app directory
WORKDIR /app
# Create app/nginx directory and copy default.conf to it
WORKDIR /app/nginx
COPY nginx/etc/conf.d/default.conf /app/nginx/
# Install app dependencies
COPY package.json package-lock.json /app/
RUN cd /app && npm set progress=false && npm install
# Copy project files into the docker image
COPY .  /app
RUN cd /app && npm run build
