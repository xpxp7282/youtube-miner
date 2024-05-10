#!/bin/bash

ARCH=$1

if [ "$ARCH" == "amd64" ]; then
    docker buildx build --platform linux/amd64 -t youtube-miner:$ARCH .
else
    docker buildx build --platform linux/arm64 -t youtube-miner:$ARCH .
fi
