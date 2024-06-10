package com.example.url_view;

import com.example.data.url_profile.UrlProfileDto;
import com.example.data.url_view.UrlViewDto;
import com.example.data.url_view.UrlViewResponse;
import com.example.url_profile.UrlProfile;
import org.springframework.stereotype.Component;

@Component
public class UrlViewMapper {
    public UrlView fromUrlViewDtoToEntity(UrlViewDto dto){
        UrlView urlView = new UrlView();
        urlView.setIpAddress(dto.getIpAddress());
        urlView.setOsSystem(dto.getOsSystem());
        urlView.setBrowser(dto.getBrowser());
        urlView.setReferer(dto.getReferer());

        return urlView;
    }

    public UrlViewResponse fromUrlViewEntityToResponse(UrlView urlView){
        return new UrlViewResponse(
                urlView.getId(),
                urlView.getUrlProfile(),
                urlView.getIpAddress(),
                urlView.getOsSystem(),
                urlView.getBrowser(),
                urlView.getReferer());
    }

    public UrlViewDto fromUrlViewResponseToDto(UrlViewResponse urlViewResponse){
        return new UrlViewDto(
                urlViewResponse.getIpAddress(),
                urlViewResponse.getOsSystem(),
                urlViewResponse.getBrowser(),
                urlViewResponse.getReferer());
    }
}
