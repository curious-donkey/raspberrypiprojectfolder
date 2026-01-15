#!/bin/bash

# Cross-compilation script using Docker
# Runs on Windows with Docker

set -e

CURRENT_DIR=$(pwd)
OUTPUT_DIR="$CURRENT_DIR/output"

echo "🐳 Cross-compiling lgpio for ARM using Docker..."

# Clean output directory
rm -rf "$OUTPUT_DIR"
mkdir -p "$OUTPUT_DIR"

# Build Docker image
echo "📦 Building cross-compilation Docker image..."
docker build -f Dockerfile.cross -t lgpio-cross .

# Run cross-compilation
echo "🔧 Running cross-compilation..."
docker run --rm \
    -v "$CURRENT_DIR:/work" \
    -v "$OUTPUT_DIR:/work/output" \
    lgpio-cross

if [ -f "$OUTPUT_DIR/liblgpio.so" ]; then
    echo "✅ Cross-compilation successful!"
    echo "📍 ARM library located at: $OUTPUT_DIR/liblgpio.so"
    
    # Show file info
    if command -v file &> /dev/null; then
        echo "🔍 Library info:"
        file "$OUTPUT_DIR/liblgpio.so"
    fi
    
    echo ""
    echo "📋 Next steps:"
    echo "1. Copy output/liblgpio.so to your Raspberry Pi"
    echo "2. Place it in your Java library path"
    echo "3. Load it in Java with: System.loadLibrary(\"lgpio\")"
else
    echo "❌ Cross-compilation failed - no output file generated"
    exit 1
fi