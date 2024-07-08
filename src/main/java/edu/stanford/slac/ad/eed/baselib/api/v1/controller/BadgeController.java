package edu.stanford.slac.ad.eed.baselib.api.v1.controller;

import edu.stanford.slac.ad.eed.baselib.service.BadgeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/v1/badge")
@AllArgsConstructor
public class BadgeController {
    private final BadgeService badgeService;

    @GetMapping("/version")
    public ResponseEntity<byte[]> generateVersionBadge() {
        String badgeUrl = badgeService.generateBadge();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "image/svg+xml");

        return new ResponseEntity<>(badgeUrl.getBytes(), headers, HttpStatus.OK);
    }
}
