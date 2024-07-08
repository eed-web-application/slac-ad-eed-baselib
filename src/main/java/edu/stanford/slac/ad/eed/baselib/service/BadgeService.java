package edu.stanford.slac.ad.eed.baselib.service;

import lombok.AllArgsConstructor;
import org.silentsoft.badge4j.Badge;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BadgeService {
    private BuildProperties buildProperties;
    /**
     * Generate a badge with the version of the application
     * @return SVG badge
     */
    public String generateBadge() {
        String badgeSVG = Badge.builder()
                .label("version")
                .color("informational")
                .message(buildProperties.getVersion())
                .build();
        return badgeSVG;
    }
}
