package com.example.urlshorteningservice.services;

import com.example.urlshorteningservice.entity.ShortLink;
import com.example.urlshorteningservice.repo.ShortLinkRepo;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;


@Service
public class ShortLinkService {
    private ShortLinkRepo shortLinkRepo;

    public ShortLinkService(ShortLinkRepo shortLinkRepo) {
        this.shortLinkRepo = shortLinkRepo;
    }

    public ShortLink createShortLink(String originalLink) {
        return Optional.ofNullable(shortLinkRepo.getShortLinkByOriginalLink(originalLink))
                .orElseGet(() -> {
                    ShortLink newLink = new ShortLink();
                    newLink.setOriginalLink(originalLink);
                    newLink.setShortLink(getShortUrl(originalLink));
                    return shortLinkRepo.save(newLink);
                });
    }

    public ShortLink getShortLinkByShortLink(String shortLink) {
        return shortLinkRepo.getShortLinkByShortLink(shortLink);
    }

    public static String getShortUrl(String url) {
        String shortUrl = Base64.getUrlEncoder().encodeToString(url.getBytes(StandardCharsets.UTF_8));
        return shortUrl.substring(0, 7);
    }

}
