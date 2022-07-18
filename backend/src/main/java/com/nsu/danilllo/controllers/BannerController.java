package com.nsu.danilllo.controllers;


import com.nsu.danilllo.controllers.requests.BannerRequest;
import com.nsu.danilllo.model.Banner;
import com.nsu.danilllo.services.impls.BannerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@RestController
public class BannerController {

    private final BannerServiceImpl bannerService;

    @Autowired
    public BannerController(BannerServiceImpl bannerService) {
        this.bannerService = bannerService;
    }

    @PostMapping("/banner/create")
    public ResponseEntity<?> createBanner(@Valid @RequestBody BannerRequest bannerRequest) {
        try {
            Long id = bannerService.createBanner(bannerRequest);
            return new ResponseEntity<>(id, HttpStatus.OK);
        } catch (EntityExistsException | NoSuchElementException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,exception.getMessage());
        }
    }

    @PostMapping("/banner/delete")
    public ResponseEntity<?> deleteBanner(@RequestParam(required = true) Long id) {
        try {
            bannerService.deleteBanner(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,exception.getMessage());
        }
    }

    @PostMapping("/banner/update")
    public ResponseEntity<?> updateBanner(
            @Valid @RequestBody BannerRequest updateRequest,
            @RequestParam(required = true) Long id) {
        try {
            Long updatedId = bannerService.updateBanner(updateRequest, id);
            return new ResponseEntity<>(updatedId, HttpStatus.OK);
        }
        catch (NoSuchElementException | EntityExistsException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,ex.getMessage());
        }
    }

    @GetMapping("/bid")
    public ResponseEntity<?> getBannerTextByCategories(
            @RequestParam("cat") List<String> requestedCategories,
            HttpServletRequest request,
            TimeZone timeZone) {
        Banner banner = bannerService.getBannerByCategories(requestedCategories, request, timeZone);
        if (banner == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(banner.getText(), HttpStatus.OK);

    }

    @GetMapping("banner/get")
    public ResponseEntity<?> getBannerById(@RequestParam(required = true, name = "id") Long id) {
        try {
            return new ResponseEntity<>(bannerService.getBannerById(id), HttpStatus.OK);
        } catch (NoSuchElementException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,exception.getMessage());
        }
    }

    @GetMapping("banner/search")
    public ResponseEntity<?> findBannersByNamePart(@RequestParam(name = "name") String namePart) {
        return new ResponseEntity<>(bannerService.findBannersByNamePart(namePart), HttpStatus.OK);
    }

}
