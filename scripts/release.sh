#!/opt/homebrew/bin/zsh

IS_RELEASE_BUILD="false"

while getopts 'r' OPT
do
  case $OPT in
    r) IS_RELEASE_BUILD="true" ;;
  esac
done

IS_RELEASE_BUILD=$IS_RELEASE_BUILD gradle clean test clean test artifactoryPublish artifactoryDeploy
