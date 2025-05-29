#!/bin/bash

echo "Building membership-service..."

# First, build the JAR file locally
echo "📦 Building JAR file locally..."
./gradlew clean :membership-service:bootJar -x test

if [ $? -ne 0 ]; then
    echo "❌ Failed to build JAR file!"
    exit 1
fi

echo "✅ JAR file built successfully!"

# Build the Docker image
echo "🐳 Building Docker image..."
cd membership-service
docker build -t membership-service:0.0.1-SNAPSHOT .
cd ..

if [ $? -eq 0 ]; then
    echo "✅ Docker image built successfully!"
    echo "Image: membership-service:0.0.1-SNAPSHOT"
    
    # Show image info
    docker images | grep membership-service
else
    echo "❌ Docker build failed!"
    exit 1
fi 