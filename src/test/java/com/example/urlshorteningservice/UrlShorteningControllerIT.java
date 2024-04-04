package com.example.urlshorteningservice;


import com.example.urlshorteningservice.repo.ShortLinkRepo;
import com.sun.source.tree.AssertTree;
import org.assertj.core.api.AssertionErrorCollector;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = UrlShorteningServiceApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class UrlShorteningControllerIT {
    @LocalServerPort
    private int port;

    @Autowired
    ShortLinkRepo shortLinkRepo;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @BeforeEach
    void reset() {
        shortLinkRepo.deleteAll();
    }

    @Test
    public void testGenShortUrl() throws JSONException {

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("URL", "https://www.bestbuy.com/site/kicker-ks-series-6-x-8-2-way-car-speakers-with-polypropylene-cones-pair-black/6406590.p?skuId=6406590");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/shorten"),
                HttpMethod.POST, entity, String.class);

        String expected = "aHR0cHM";

        assertEquals(expected, response.getBody().toString());
    }

    @Test
    public void testGenAndGetShortUrl() throws JSONException {

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("URL", "https://www.bestbuy.com/site/kicker-ks-series-6-x-8-2-way-car-speakers-with-polypropylene-cones-pair-black/6406590.p?skuId=6406590");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/shorten"),
                HttpMethod.POST, entity, String.class);

        String expected = "aHR0cHM";

        assertEquals(expected, response.getBody().toString());

        response = restTemplate.exchange(
                createURLWithPort("/aHR0cHM"),
                HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatusCode.valueOf(302), response.getStatusCode());
    }

    @Test
    public void testGetNotExistantShortUrl() throws JSONException {

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/aHR0cHM"),
                HttpMethod.GET, null, String.class);

        assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}