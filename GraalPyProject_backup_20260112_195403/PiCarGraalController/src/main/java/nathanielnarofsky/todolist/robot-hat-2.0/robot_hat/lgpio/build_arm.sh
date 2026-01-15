#!/bin/bash

# Build script for ARM/Linux (Raspberry Pi)
# Run this script on your Raspberry Pi

set -e  # Exit on any error

echo "Building lgpio native library for ARM/Linux..."

# Check if we have required tools
if ! command -v gcc &> /dev/null; then
    echo "GCC not found. Installing build tools..."
    sudo apt update
    sudo apt install -y build-essential
fi

# Set variables
JAVA_HOME=${JAVA_HOME:-/usr/lib/jvm/default-java}
LIB_NAME="liblgpio"
PACKAGE_PATH="nathanielnarofsky/todolist"

# Check if JAVA_HOME is set and JNI headers exist
if [ ! -d "$JAVA_HOME/include" ]; then
    echo "Warning: JNI headers not found at $JAVA_HOME/include"
    echo "Please install OpenJDK development package:"
    echo "  sudo apt install openjdk-11-jdk-headless"
    echo "And set JAVA_HOME properly"
    exit 1
fi

# Detect architecture
ARCH=$(uname -m)
case $ARCH in
    armv6l|armv7l)
        JNI_ARCH="linux"
        ;;
    aarch64)
        JNI_ARCH="linux"
        ;;
    x86_64)
        JNI_ARCH="linux"
        ;;
    *)
        echo "Unsupported architecture: $ARCH"
        exit 1
        ;;
esac

echo "Building for architecture: $ARCH"
echo "Java home: $JAVA_HOME"

# Compile the native library
echo "Compiling native library..."
gcc -shared -fPIC \
    -I"$JAVA_HOME/include" \
    -I"$JAVA_HOME/include/$JNI_ARCH" \
    -o "$LIB_NAME.so" \
    lgpio_wrap.c src/lgpio.c \
    -lpthread

if [ $? -eq 0 ]; then
    echo "✅ Successfully built $LIB_NAME.so"
    echo "Library info:"
    ls -la "$LIB_NAME.so"
    file "$LIB_NAME.so"
else
    echo "❌ Compilation failed"
    exit 1
fi

echo "Done! Copy $LIB_NAME.so to your Java library path."