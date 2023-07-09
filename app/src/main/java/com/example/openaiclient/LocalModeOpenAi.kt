package com.example.openaiclient

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.logging.LogLevel
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.LoggingConfig
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import com.example.openaiclient.bean.RequestBean
import com.example.openaiclient.bean.RespBean
import com.example.openaiclient.listener.OpenAiListener
import com.example.openaiclient.util.ThreadPoolManager
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.seconds

 class LocalModeOpenAi : OpenAiImpl {
    private lateinit var openAI: OpenAI
    override fun init(apiKey: String) {
        val config = OpenAIConfig(
            token = apiKey,
            timeout = Timeout(socket = 60.seconds),
            logging = LoggingConfig(logLevel = LogLevel.All)

            )
        openAI = OpenAI(config)
    }

    override fun request(req: RequestBean, callback: OpenAiListener) {
        ThreadPoolManager.executeTask(object : Runnable {
            override fun run() {
                try {
                    realRequest(req, callback)
                } catch (e: Exception) {
                    e.printStackTrace()
                    callback.onFail(e.message)
                }
            }
        })

    }

    @OptIn(BetaOpenAI::class)
    fun realRequest(req: RequestBean, callback: OpenAiListener) = runBlocking<Unit> {
        var list: List<ChatMessage> = listOf()
        for (item: RequestBean.ContentsDTO in req.contents) {
            var chatRole: ChatRole;
            if (item.role.equals("user")) {
                chatRole = ChatRole.User
            } else if (item.role.equals("system")) {
                chatRole = ChatRole.System
            } else {
                chatRole = ChatRole.Assistant
            }

            var msg: ChatMessage = ChatMessage(role = chatRole, content = item.content)
            list = list.plus(msg)
        }

        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId("gpt-3.5-turbo"),
            maxTokens = req.maxTokens,
            messages = list
        )
        val completion: ChatCompletion = openAI.chatCompletion(chatCompletionRequest)
        val msg = completion.choices.get(0).message
        var respBean = RespBean()
        respBean.code = 200
        respBean.message = msg?.content
        callback.onSuccess(respBean)
    }

}