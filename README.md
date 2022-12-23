测试命令：
POST http://localhost:8080/api/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin"
}

GET http://localhost:8080/api/user/all
Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NzE4OTIyMTYsInVzZXJJRCI6MSwidXNlcm5hbWUiOiJhZG1pbiJ9.x-olY73ycXfEHhNTADhf7P8htrIseyDCq47RVRKDG_c
