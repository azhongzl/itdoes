title Itdoes

@echo off
echo Start...

pushd %~dp0

cd ..
call mvn install -Dmaven.test.skip=true

popd
