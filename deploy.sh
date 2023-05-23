#!/bin/bash
# Author: Edwin Martinez Jr


# shellcheck disable=SC2034
error="🔴"
passed="🟢"
module="$1"
tag_message="$2"

function main() {
    if [ ! -d "$module" ]; then
      echo "Please specify a valid module"
    else
        should_be_library
        should_have_clean_git
        show_deploying_message
        unit_test
        upload
        add_git_tag
    fi
}

function should_be_library() {
    version=$(cat "$module"/version.txt)
    # shellcheck disable=SC2181
    if [ $? -ne 0 ]; then
      exit 1
    fi
}

function should_have_clean_git() {
    # shellcheck disable=SC2046
    if [ ! $(git status --porcelain | wc -l) -eq "0" ]; then
      echo "  🔴 Git repo dirty. Please commit and push all your changes first."
      exit 1
    fi
}

function show_deploying_message() {
    version=$(cat "$module"/version.txt)
    echo "Deploying $version for $module..."
}

function unit_test() {
    ./gradlew :"$module":testDebugUnitTest
    # shellcheck disable=SC2181
    if [ $? -ne 0 ]; then
      exit 1
    fi
}

function upload() {
    ./gradlew "$module":clean
    ./gradlew "$module":build
    ./gradlew "$module":publish

    if [ $? -ne 0 ]; then
      exit 1
    fi
}

function add_git_tag() {
    git push origin
    version=$(cat "$module"/version.txt)
    message="Release $version"
    if [[ "$tag_message" == *".txt"* ]]; then # Check if contains .txt
      if [ -f "$tag_message" ]; then # Check if file exists
        msg=$(cat "$tag_message")
        if [ ! -z "$message" ]; then # Check if file contains something
          message=$msg
        fi
      fi
    elif [ ! -z "$tag_message" ]; then # Check if empty string
        message=$tag_message
    fi
    tag="${module}_v$version"
    git tag -a "$tag" -m "$message"
    git push origin "$tag"
}

main