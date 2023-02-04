一套springboot+Jwt+mybatis的登陆系统，仅后端  
整合了druid数据源和swagger接口管理页面  
核心代码有详细注释  
包含建库sql脚本  

测试命令，使用IDEA自带的http测试工具：  
POST http://localhost:8080/api/login  
Content-Type: application/json  
  
{  
  "username": "admin",  
  "password": "admin"  
}  
  
GET http://localhost:8080/api/user/all  
Authorization: Bearer 这里替换为token字段  
