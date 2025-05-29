#!/bin/bash

echo "Stopping Payment System services..."

# Stop and remove containers
docker compose down

if [ $? -eq 0 ]; then
    echo "✅ Services stopped successfully!"
    
    # Ask if user wants to remove volumes (data)
    read -p "Do you want to remove volumes (this will delete all data)? [y/N]: " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        echo "Removing volumes..."
        docker compose down -v
        echo "✅ Volumes removed"
    fi
    
    # Ask if user wants to remove images
    read -p "Do you want to remove Docker images? [y/N]: " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        echo "Removing images..."
        docker rmi membership-service:0.0.1-SNAPSHOT 2>/dev/null || true
        docker rmi axoniq/axonserver:2023.2.0 2>/dev/null || true
        docker rmi mysql:8.0 2>/dev/null || true
        echo "✅ Images removed"
    fi
    
else
    echo "❌ Failed to stop services"
    exit 1
fi 