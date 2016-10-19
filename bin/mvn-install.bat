title Itdoes

@echo off
echo Start...

pushd %~dp0

cd ..
call git pull
call mvn install

popd