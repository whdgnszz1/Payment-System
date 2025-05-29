#!/bin/bash

echo "Starting Payment System with Docker Compose..."

if [ ! -f .env ]; then
    echo "Creating .env file..."
    cat > .env << EOF
# MySQL Configuration
MYSQL_ROOT_PASSWORD=rootpassword
MYSQL_DATABASE=payment
MYSQL_USER=test
MYSQL_PASSWORD=test

# Timezone
TZ=Asia/Seoul
EOF
    echo "✅ .env file created"
fi

# Build the application first
echo "Building membership-service Docker image..."
./docker-build.sh

if [ $? -ne 0 ]; then
    echo "❌ Failed to build Docker image"
    exit 1
fi

# Start services
echo "Starting services with Docker Compose..."
docker compose up -d

if [ $? -eq 0 ]; then
    echo "✅ Services started successfully!"
    echo ""
    echo "🔗 Available endpoints:"
    echo "   - Membership Service: http://localhost:9000"
    echo "   - Swagger UI: http://localhost:9000/swagger-ui/index.html"
    echo "   - Axon Server: http://localhost:8024"
    echo "   - MySQL: localhost:3306"
    echo ""
    echo "📊 Check service status:"
    echo "   docker compose ps"
    echo ""
    echo "📋 View logs:"
    echo "   docker compose logs -f membership-service"
else
    echo "❌ Failed to start services"
    exit 1
fi 