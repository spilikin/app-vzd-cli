@echo off
set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
"%DIRNAME%\vzd-cli.bat" gui
