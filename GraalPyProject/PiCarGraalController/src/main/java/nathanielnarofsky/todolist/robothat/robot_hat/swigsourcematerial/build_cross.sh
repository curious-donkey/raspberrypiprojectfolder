#!/bin/bash

set -e

echo "Cross-compiling lgpio for ARM/Linux..."

# Set variables
JAVA_HOME=${JAVA_HOME:-/usr/lib/jvm/java-17-openjdk-amd64}
LIB_NAME="liblgpio"

echo "Using JAVA_HOME: $JAVA_HOME"
echo "Architecture: $(uname -m)"

# Check for required files
if [ ! -f "lgpio_wrap.c" ]; then
    echo "Error: lgpio_wrap.c not found"
    exit 1
fi

if [ ! -d "src" ]; then
    echo "Error: src directory not found"
    exit 1
fi

# Find all source files (following setup.py pattern)
SRC_FILES=$(find src -name "lg*.c" -o -name "rgpiod.c")
if [ -z "$SRC_FILES" ]; then
    echo "Error: No source files found in src/"
    exit 1
fi

echo "Found source files:"
echo "$SRC_FILES"

if [ ! -d "$JAVA_HOME/include" ]; then
    echo "Error: JNI headers not found at $JAVA_HOME/include"
    exit 1
fi

# Cross-compile for ARM
echo "Cross-compiling..."
${CROSS_TRIPLE}-gcc -shared -fPIC \
    -I"$JAVA_HOME/include" \
    -I"$JAVA_HOME/include/linux" \
    -Isrc \
    -o "$LIB_NAME.so" \
    lgpio_wrap.c $SRC_FILES \
    -lpthread -lrt -ldl

if [ $? -eq 0 ]; then
    echo "✅ Successfully built $LIB_NAME.so"
    echo "Library info:"
    ls -la "$LIB_NAME.so"
    file "$LIB_NAME.so"
    
    # Copy to output directory
    mkdir -p /work/output
    cp "$LIB_NAME.so" /work/output/
    echo "Copied to /work/output/"
else
    echo "❌ Cross-compilation failed"
    exit 1
fi