package com.ssafy.bookkoo.bookservice.util.AladinAPI;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.net.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AladinAPIHandler {

    final private AladinCategoryService aladinCategoryService;
    private static String apiKey;

    @Value("ttbwintiger981754003")
    public void setApiKey(String apiKey) {
        AladinAPIHandler.apiKey = apiKey;
    }

    private static final String BASE_URL = "http://www.aladin.co.kr/ttb/api/";

    /**
     * 알라딘 API에서 책 데이터를 검색해 가져오는 메서드
     *
     * @param params : 검색어(AladinAPISearchParams)
     * @return JSONObject : API 응답 데이터
     * @throws IOException
     * @throws InterruptedException
     * @throws URISyntaxException
     */
    public ResponseAladinAPI searchBooksFromAladin(AladinAPISearchParams params)
        throws IOException, InterruptedException, URISyntaxException {
        URI uri = new URIBuilder(BASE_URL + "itemSearch.aspx")
            .addParameter("ttbkey", apiKey)
            .addParameter("Query", params.getQuery())
            .addParameter("QueryType", String.valueOf(params.getQueryType()))
            .addParameter("MaxResults", String.valueOf(params.getMaxResult()))
            .addParameter("start", String.valueOf(params.getStart()))
            .addParameter("SearchTarget", "Book")
            .addParameter("output", "JS")
            .addParameter("Version", "20131101")
            .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(uri)
                                         .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            ObjectMapper objectMapper = new ObjectMapper();
            ResponseAladinAPI apiResponse = objectMapper.readValue(response.body(),
                ResponseAladinAPI.class);
            // 카테고리 매핑 적용
            aladinCategoryService.processApiResponse(apiResponse);

            return apiResponse;
        } else {
            throw new IOException("Failed to fetch data from Aladin API: " + response.body());
        }
    }
}
