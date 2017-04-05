echo 进入webpack目录!
e:
cd nbamanager
cd dist
del /s /q /f *.*
for /d %%i in (*) do rd /s /q "%%i"
echo 已删除dist目录下文件!
cd ..
call webpack
echo webpack打包完成!
cd ..
cd nbamanager-server/src/main/resources/static
copy favicon.ico ..\
del /s /q /f *.*
for /d %%i in (*) do rd /s /q "%%i"
echo 已删除static目录下文件!
cd ..
copy favicon.ico .\static\favicon.ico
del /q favicon.ico
xcopy ..\..\..\..\nbamanager\dist .\static /e
cd ..\..\..\
call gradle build
echo gradle打包完成!
pause
