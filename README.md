#### 技术栈
1. okhttp
2. room
3. jtokkit
   用于token数计算
4. lottie
   动画展示
5. openai-client
   用于本地模式

部分代码由`GitHub Copilot`生成,大部分图片由`Stable Diffusion WebUI`生成.

#### 客户端功能
1. 自定义`api_key`.
2. 本地模式(使用自己`api_key`,从本地发出请求,不使用服务器转发)
3. 隐私安全.
   聊天记录存本地,可删除. 不收集任何隐私信息.
4. 自定义最大token数.
5. 自定义内容记录长度.
6. 支持`prompts`