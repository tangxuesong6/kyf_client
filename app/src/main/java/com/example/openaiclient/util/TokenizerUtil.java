package com.example.openaiclient.util;

import com.example.openaiclient.bean.RequestBean;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.EncodingType;

import java.util.List;

public class TokenizerUtil {
    public static int countMessageTokens(
            EncodingRegistry registry,
            String model,
            List<RequestBean.ContentsDTO> messages // consists of role, content and an optional name
    ) {
        Encoding encoding;
        int tokensPerMessage;
        int tokensPerName;
        if (model.startsWith("gpt-4")) {
            tokensPerMessage = 3;
            tokensPerName = 1;
            encoding = registry.getEncoding(EncodingType.CL100K_BASE);
        } else if (model.startsWith("gpt-3.5-turbo")) {
            tokensPerMessage = 4; // every message follows <|start|>{role/name}\n{content}<|end|>\n
            tokensPerName = -1; // if there's a name, the role is omitted
            encoding = registry.getEncoding(EncodingType.CL100K_BASE);
        } else {
            throw new IllegalArgumentException("Unsupported model: " + model);
        }

        int sum = 0;
        for (RequestBean.ContentsDTO  message : messages) {
            sum += tokensPerMessage;
            sum += encoding.countTokens(message.content);
            sum += encoding.countTokens(message.role);
//            if (message.hasName()) {
//                sum += encoding.countTokens(message.getName());
//                sum += tokensPerName;
//            }
        }

        sum += 3; // every reply is primed with <|start|>assistant<|message|>

        return sum;
    }
}
