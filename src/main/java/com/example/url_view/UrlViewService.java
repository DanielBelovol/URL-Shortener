package com.example.url_view;

import com.example.data.url_profile.UrlProfileResponse;
import com.example.data.url_view.UrlViewDto;
import com.example.data.url_view.UrlViewResponse;
import com.example.exceptions.UserNotFoundException;
import com.example.url_profile.UrlProfileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UrlViewService {
    private UrlViewRepository urlViewRepository;
    private UrlViewMapper urlViewMapper;
    private UrlProfileMapper urlProfileMapper;

    @Autowired
    public UrlViewService(UrlViewRepository urlViewRepository, UrlViewMapper urlViewMapper, UrlProfileMapper urlProfileMapper) {
        this.urlViewRepository = urlViewRepository;
        this.urlViewMapper = urlViewMapper;
        this.urlProfileMapper = urlProfileMapper;
    }

    public UrlViewResponse saveUrlView(UrlViewDto dto, UrlProfileResponse urlProfileResponse) throws UserNotFoundException {
        UrlView urlView = urlViewMapper.fromUrlViewDtoToEntity(dto);

        urlView.setUrlProfile(urlProfileMapper.fromUrlProfileResponseToEntity(urlProfileResponse));

        return urlViewMapper.fromUrlViewEntityToResponse(urlViewRepository.save(urlView));
    }

    public List<UrlViewResponse> getViewsForUrl(long id){
        return urlViewRepository.findAllByUrlId(id).stream()
                .map(urlViewMapper::fromUrlViewEntityToResponse)
                .collect(Collectors.toList());
    }
    public Long getCountOfRedirectsForUrl(long id){
        return urlViewRepository.countByUrlId(id);
    }
}
