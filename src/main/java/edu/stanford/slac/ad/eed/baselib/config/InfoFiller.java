package edu.stanford.slac.ad.eed.baselib.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class InfoFiller implements InfoContributor {
    private BuildProperties buildProperties;
    @Override
    public void contribute(Info.Builder builder) {
//        InfoDTO info = InfoDTO
//                .builder()
//                .name(buildProperties.getName())
//                .version(buildProperties.getVersion())
//                .build();
//
//        builder.withDetail("app", info);
    }
}