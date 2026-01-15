#!/bin/bash

# Package script - creates a bundle for Raspberry Pi compilation
# Run this on Windows (in Git Bash) or Linux

BUNDLE_DIR="lgpio_bundle"
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
BUNDLE_NAME="lgpio_arm_${TIMESTAMP}.tar.gz"

echo "Creating bundle for ARM compilation..."

# Clean up previous bundle
rm -rf "$BUNDLE_DIR"
mkdir -p "$BUNDLE_DIR"

# Copy necessary files
echo "Copying files..."
cp lgpio_wrap.c "$BUNDLE_DIR/"
cp build_arm.sh "$BUNDLE_DIR/"
cp -r src "$BUNDLE_DIR/" 2>/dev/null || echo "Warning: src directory not found"

# Copy Java files for reference
mkdir -p "$BUNDLE_DIR/java"
cp *.java "$BUNDLE_DIR/java/" 2>/dev/null || true

# Create README
cat > "$BUNDLE_DIR/README.md" << 'EOF'
# LGPIO ARM Compilation Bundle

## Prerequisites on Raspberry Pi:
```bash
sudo apt update
sudo apt install -y build-essential openjdk-11-jdk-headless
```

## Build Instructions:
1. Extract this bundle on your Raspberry Pi
2. Set JAVA_HOME if needed:
   ```bash
   export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-armhf
   ```
3. Run the build script:
   ```bash
   chmod +x build_arm.sh
   ./build_arm.sh
   ```
4. Copy the generated `liblgpio.so` to your Java project's library path

## Files included:
- `lgpio_wrap.c` - SWIG-generated C wrapper
- `src/` - Original lgpio C source code  
- `build_arm.sh` - Build script for ARM/Linux
- `java/` - Generated Java classes for reference
EOF

# Create the bundle
echo "Creating archive..."
tar -czf "$BUNDLE_NAME" -C . "$BUNDLE_DIR"

echo "✅ Bundle created: $BUNDLE_NAME"
echo "📦 Size: $(du -h $BUNDLE_NAME | cut -f1)"
echo ""
echo "Next steps:"
echo "1. Copy $BUNDLE_NAME to your Raspberry Pi"
echo "2. Extract: tar -xzf $BUNDLE_NAME"
echo "3. Run: cd $BUNDLE_DIR && ./build_arm.sh"

# Clean up
rm -rf "$BUNDLE_DIR"