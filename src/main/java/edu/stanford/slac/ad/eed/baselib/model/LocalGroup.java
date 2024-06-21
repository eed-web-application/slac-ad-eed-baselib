package edu.stanford.slac.ad.eed.baselib.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LocalGroup {
    @Id
    String id;
    String description;
    List<String> userIds;
}
