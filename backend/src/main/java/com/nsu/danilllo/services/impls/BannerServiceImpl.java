package com.nsu.danilllo.services.impls;

import com.nsu.danilllo.controllers.dto.BannerDto;
import com.nsu.danilllo.controllers.mappers.BannerMapper;
import com.nsu.danilllo.controllers.requests.BannerRequest;
import com.nsu.danilllo.model.Banner;
import com.nsu.danilllo.model.NoContentReason;
import com.nsu.danilllo.repositories.BannerRepository;
import com.nsu.danilllo.model.Category;
import com.nsu.danilllo.repositories.CategoryRepository;
import com.nsu.danilllo.services.BannerService;
import com.nsu.danilllo.services.LogService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BannerServiceImpl implements BannerService {
    private final BannerRepository bannerRepository;
    private CategoryRepository categoryRepository;
    private LogService logService;

    public BannerServiceImpl(BannerRepository bannerRepository, CategoryRepository categoryRepository, LogService logService) {
        this.bannerRepository = bannerRepository;
        this.categoryRepository = categoryRepository;
        this.logService = logService;
    }

    public Long createBanner(BannerRequest bannerRequest) throws EntityExistsException, NoSuchElementException {
        if (bannerRepository.findByNameAndDeletedFalse(bannerRequest.getName()).isPresent()) {
            throw new EntityExistsException("Banner with " + bannerRequest.getName() + " already exists");
        }

        Banner banner = new Banner();
        banner.setName(bannerRequest.getName());
        banner.setPrice(bannerRequest.getPrice());
        banner.setText(bannerRequest.getText());
        banner.setCategories(getCategoriesFromRequest(bannerRequest));

        bannerRepository.save(banner);

        return banner.getId();
    }


    public Long updateBanner(BannerRequest bannerRequest, Long id) throws NoSuchElementException {
        Banner banner = bannerRepository.findByIdAndDeletedFalse(id).
                orElseThrow(() -> new NoSuchElementException("No such banner"));
        if (isAnotherBannerWithSuchNameExists(banner, bannerRequest.getName())) {
            throw new EntityExistsException("Banner already exists\n");
        }
        banner.setName(bannerRequest.getName());
        banner.setText(bannerRequest.getText());
        banner.setPrice(bannerRequest.getPrice());
        banner.setCategories(getCategoriesFromRequest(bannerRequest));
        bannerRepository.save(banner);

        return banner.getId();
    }


    public void deleteBanner(Long id) throws NoSuchElementException {
        Banner bannerFromDB = bannerRepository.findByIdAndDeletedFalse(id).
                orElseThrow(() -> new NoSuchElementException("No such element"));
        bannerFromDB.setDeleted(true);
        bannerRepository.save(bannerFromDB);
    }

    public Banner getBannerByCategories(List<String> requestIds,
                                        HttpServletRequest request, TimeZone timeZone) {

        List<Category> requestedCategories = getCategoriesByRequests(requestIds);
        TreeSet<Banner> acceptableBanners = getBannersByCategories(requestedCategories);
        List<Banner> todayShownBanners = logService.getLastShownBanners(request, timeZone);
        Banner chosenBanner = null;
        for (Banner banner : acceptableBanners) {
            if (!todayShownBanners.contains(banner)) chosenBanner = banner;
        }
        NoContentReason reason = logService.getNoContentReason(chosenBanner, acceptableBanners);
        logService.saveLog(chosenBanner, requestedCategories, request, reason);
        return chosenBanner;

    }

    public Banner getBannerById(Long id) throws NoSuchElementException {
        return bannerRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No banner with such id"));
    }

    public List<BannerDto> findBannersByNamePart(String namePart) {
        return bannerRepository.findBannersByName(namePart).stream().map(BannerMapper::toDto).collect(Collectors.toList());
    }

    private boolean isAnotherBannerWithSuchNameExists(Banner banner, String name) {
        return banner != bannerRepository.findByNameAndDeletedFalse(name).orElse(banner);
    }

    private List<Category> getCategoriesByRequests(List<String> requests) {
        List<Category> requestedCategories = new LinkedList<>();
        for (String requestId : requests) {
            categoryRepository.findByRequestedIDAndDeletedFalse(requestId).
                    ifPresent(requestedCategories::add);
        }

        return requestedCategories;
    }

    private TreeSet<Banner> getBannersByCategories(List<Category> categories) {
        TreeSet<Banner> acceptableBanners = new TreeSet<>();

        for (Category category : categories) {
            List<Banner> banners = bannerRepository.findByCategoriesContainingAndDeletedFalse(category);
            acceptableBanners.addAll(banners);
        }

        return acceptableBanners;
    }

    private List<Category> getCategoriesFromRequest(BannerRequest bannerRequest) {
        List<Category> categories = bannerRequest.getCategoriesIDs().stream().
                map((i) -> categoryRepository.findByIdAndDeletedFalse((i)).
                        orElseThrow(() -> new NoSuchElementException("Bad id for category")))
                .collect(Collectors.toList());
        if (categories.isEmpty()) throw new NoSuchElementException("Categories list is empty");
        return categories;
    }
}
