package com.example.url_view;

import com.example.data.url_view.UrlViewDto;
import com.example.data.url_view.UrlViewResponse;
import com.example.testcontainer.BaseTestWithPostgresContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UrlViewMapperTest extends BaseTestWithPostgresContainer {
    UrlViewMapper urlViewMapper = new UrlViewMapper();

    @Test
    void fromUrlViewDtoToEntityTest() {
        UrlViewDto urlViewDto = new UrlViewDto("12.40.51.18", "Windows", "Chrome", " - ");

        UrlView actualEntity = urlViewMapper.fromUrlViewDtoToEntity(urlViewDto);

        UrlView expectedEntity = new UrlView(0, null, "12.40.51.18", "Windows", "Chrome", " - ");

        Assertions.assertEquals(expectedEntity, actualEntity);
    }

    @Test
    void fromUrlViewEntityToResponseTest() {
        UrlView urlView = new UrlView(5, null, "12.40.51.18", "Windows", "Chrome", " - ");

        UrlViewResponse actualResponse = urlViewMapper.fromUrlViewEntityToResponse(urlView);

        UrlViewResponse expectedResponse = new UrlViewResponse(5, null, "12.40.51.18", "Windows", "Chrome", " - ");

        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void fromUrlViewResponseToDtoTest() {
        UrlViewResponse response = new UrlViewResponse(6, null, "12.41.52.18", "Mac", "Firefox", " - ");

        UrlViewDto actualDto = urlViewMapper.fromUrlViewResponseToDto(response);

        UrlViewDto expectedDto = new UrlViewDto("12.41.52.18", "Mac", "Firefox", " - ");

        Assertions.assertEquals(expectedDto, actualDto);
    }
}