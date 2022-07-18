package com.nsu.danilllo.services;

import com.nsu.danilllo.model.Banner;
import com.nsu.danilllo.model.Category;
import com.nsu.danilllo.model.NoContentReason;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

public interface LogService {

    void saveLog(Banner banner, List<Category> categoryList, HttpServletRequest request, NoContentReason reason);


    /***
     *
     * @param request - запрос, из которого можно получить user-agent и IP
     * @param timeZone - для получения результата, который соответствует временной зоне пользователя
     * @return - последний выданный пользователю баннер
     */
    List<Banner> getLastShownBanners(HttpServletRequest request, TimeZone timeZone);

    NoContentReason getNoContentReason(Banner chosenBanner, Set<Banner> acceptableBannersByRequest);
}
