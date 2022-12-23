一套springboard+Jwt+mybatis的登陆系统，仅后端
整合了druid数据源和swagger接口管理页面
核心代码有详细注释
包含建库sql脚本

测试命令：
POST http://localhost:8080/api/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin"
}

GET http://localhost:8080/api/user/all
Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NzE4OTIyMTYsInVzZXJJRCI6MSwidXNlcm5hbWUiOiJhZG1pbiJ9.x-olY73ycXfEHhNTADhf7P8htrIseyDCq47RVRKDG_c
