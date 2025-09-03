package br.com.alura.screenmatchSpring.Service;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

public class ConsultaChatGPT {
    public static String obterTraducao(String texto) {
        OpenAIClient client = OpenAIOkHttpClient.builder()
//                .apiKey("")
                .build();
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .addUserMessage("Traduza para o português: " + texto)
                .model(ChatModel.GPT_4_1)
                .build();

        ChatCompletion completion = client.chat().completions().create(params);
        return completion.choices().get(0).message().content().orElse("");
    }
}
