package lk.slife.policyreportexporter.clients;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AusysWebClient {

    private final WebClient webClient;

    @Value("${ausys.client.proposal-form.url}")
    private String proposalPdfUrl;


    public byte[] getProposalPdf(String proposalId) {

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("proposalNo", proposalId);

        return webClient.post()
                .uri(proposalPdfUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_PDF)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .map(error -> {
                                    log.error("AUSYS API Error: {}", error);
                                    return new RuntimeException("AUSYS API Error: " + error);
                                })
                )
                .bodyToMono(byte[].class)
                .block();
    }
}