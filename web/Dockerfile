# Step 1: Build the application
FROM node:18 as build

# Set the working directory
WORKDIR /app

# Install dependencies
COPY package*.json ./
RUN npm install

# Copy the rest of the application code
COPY . .

RUN npm run build

EXPOSE 3000

ENTRYPOINT ["npm", "run", "dev"]