echo ����webpackĿ¼!
e:
cd nbamanager
cd dist
del /s /q /f *.*
for /d %%i in (*) do rd /s /q "%%i"
echo ��ɾ��distĿ¼���ļ�!
cd ..
call webpack
echo webpack������!
cd ..
cd nbamanager-server/src/main/resources/static
copy favicon.ico ..\
del /s /q /f *.*
for /d %%i in (*) do rd /s /q "%%i"
echo ��ɾ��staticĿ¼���ļ�!
cd ..
copy favicon.ico .\static\favicon.ico
del /q favicon.ico
xcopy ..\..\..\..\nbamanager\dist .\static /e
cd ..\..\..\
call gradle build
echo gradle������!
pause
