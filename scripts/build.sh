#!/opt/homebrew/bin/zsh

IS_RELEASE_BUILD="false"

while getopts 'rh' OPT
do
  case $OPT in
    r)IS_RELEASE_BUILD="true"
      ;;
    h)echo "Usage: $0 [-r]"
      echo "\nBuild and deploy to Artifactory. By default a snapshot version is built."
      echo "\nOptions:\n\t-r) Build and deploy a release version"
      exit(0)
      ;;
  esac
done

IS_RELEASE_BUILD=$IS_RELEASE_BUILD gradle clean test clean test artifactoryPublish artifactoryDeploy
