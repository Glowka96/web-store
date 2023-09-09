# STEP 1: Build static website
FROM gcr.io/google-appengine/nodejs AS builder

# Create app directory
WORKDIR /app

# Install app dependencies
COPY package.json package-lock.json /app/
RUN npm set progress=false && npm install

# Copy project files into the docker image
COPY . /app

# Build the Angular app
RUN npm run build

# STEP 2: Build a small nginx image with the static website
FROM nginx:alpine

# Remove default nginx website
RUN rm -rf /usr/share/nginx/html/*

# From 'builder' copy the website to the default nginx public folder
COPY --from=builder /app/dist/web-store-angular /usr/share/nginx/html

# Expose port
EXPOSE 80

# Start nginx with daemon off
CMD ["nginx", "-g", "daemon off;"]