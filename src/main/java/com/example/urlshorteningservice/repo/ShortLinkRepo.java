package com.example.urlshorteningservice.repo;

import com.example.urlshorteningservice.entity.ShortLink;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortLinkRepo extends CrudRepository<ShortLink, Integer> {

    ShortLink getShortLinkByShortLink(String shortLink);

    ShortLink getShortLinkByOriginalLink(String originalLink);

}
