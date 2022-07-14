package com.nsu.danilllo.services;

import com.nsu.danilllo.controllers.requests.BannerRequest;
import com.nsu.danilllo.model.Banner;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.TimeZone;

public interface BannerService {
    /***
     * @param bannerRequest - body http запроса в виде json'a
     * @return id созданного баннера
     */
    Long createBanner(BannerRequest bannerRequest);

    /***
     * @param id - ID обновляемого баннера
     * @param request - body http запроса в виде json'a
     * @return id обновлённого
     */
    Long updateBanner(BannerRequest request, Long id);

    void deleteBanner(Long id);

    /***
     * Метод, ищущий лучший баннер, который содержит хотя бы одну категорию из перечисленных в @param requestIDs
     * @param requestIDs - IDs запрашиваемых категорий
     * @param request - HttpServletRequest для получения Ip и User-Agent
     * @param timeZone - для выдачи пользавателю результата, который соответсвует его временной зоне
     * @return - баннер с наилучшей ценой и который не показывался прошлый в этот день для данного пользователя
     */
    Banner getBannerByCategories(List<String> requestIDs, HttpServletRequest request, TimeZone timeZone);


    Banner getBannerById(Long id);

    /***
     * Поиск баннеров по шаблону имени
     * @param namePart - шаблон поиска именни баннера
     * @return - список подходящих баннеров
     */
    List<Banner> findBannersByNamePart(String namePart);
}
