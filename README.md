# SpringSecurity 流程



```mermaid
graph LR
Filter 
--> AuthManager 
--> Provider
--> userDetailsService

Provider --> 认证逻辑,返回认证成功的结果
```
