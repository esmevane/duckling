#!/usr/bin/env bash

GRADLE=`which gradle`
WRAPPER="./gradlew"

[ -e "$GRADLE" ] && echo "$GRADLE" || echo "$WRAPPER"
