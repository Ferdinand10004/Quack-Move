#!/usr/bin/env sh
set -eu
cd "$(dirname "$0")"
if [ ! -f gradle/wrapper/gradle-wrapper.jar ]; then
  printf '%s\n' 'Descarga gradle/wrapper/gradle-wrapper.jar del repositorio original antes de usar ./gradlew.'
  exit 1
fi
exec java -classpath gradle/wrapper/gradle-wrapper.jar org.gradle.wrapper.GradleWrapperMain "$@"
