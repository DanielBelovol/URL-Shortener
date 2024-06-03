package com.example.url_view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UrlViewService {
    private UrlViewRepository urlViewRepository;

    @Autowired
    public UrlViewService(UrlViewRepository urlViewRepository) {
        this.urlViewRepository = urlViewRepository;
    }

    public void saveUrlView(UrlView urlView){
        urlViewRepository.save(urlView);
    }

    public List<UrlView> getViewsForUrl(long id){
        return urlViewRepository.findAllByUrlId(id);
    }
    public Long getCountOfRedirectsForUrl(long id){
        return urlViewRepository.countByUrlId(id);
    }
}
