package com.example.urlshorteningservice.controller;

import com.example.urlshorteningservice.entity.ShortLink;
import com.example.urlshorteningservice.services.ShortLinkService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class LinkController {

    @Autowired
    ShortLinkService shortLinkService;
    @PostMapping ("/shorten")
    @ResponseBody
    public String shorten(@RequestParam String URL) {
        ShortLink shortLink = shortLinkService.createShortLink(URL);
        return shortLink.getShortLink();
    }

    @GetMapping ("/{shortUrl}")
    public void retrieve(@PathVariable("shortUrl") String shortUrl, HttpServletResponse httpServletResponse) {
        ShortLink shortLink = shortLinkService.getShortLinkByShortLink(shortUrl);
        Optional.ofNullable(shortLink)
                .map(shortLink1 -> {
                    httpServletResponse.setHeader("Location", shortLink1.getOriginalLink());
                    httpServletResponse.setStatus(302);
                    return httpServletResponse;
                })
                        .orElseGet(() -> {
                            httpServletResponse.setStatus(404);
                            return httpServletResponse;
                        });
    }
}