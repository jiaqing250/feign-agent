
#### 使用场景
springcloud+openfeign 多服务互相调用的情况下使用

开发的接口涉及到多个模块时，每次都需要手动启动依赖的模块，feign-agent 通过字节注入修改原代码逻辑，可以获取当前配置指定服务的host

#### 使用教程

1. 下载jar包，放在指定目录 例如： C:\other\agent\feign\feign-agent.jar
2. 在 idea 项目参数配置
   ![](.\img\img.png "修改配置")

**vm options**
> -javaagent:C:\other\agent\feign\feign-agent.jar
   
**Environment variables**
> feign.url=你服务的地址


最后启动服务，日志开头打印了  "agent feign start."  就说明成功了
