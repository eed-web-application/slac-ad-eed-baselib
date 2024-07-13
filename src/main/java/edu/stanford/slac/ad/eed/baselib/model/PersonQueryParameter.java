package edu.stanford.slac.ad.eed.baselib.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PersonQueryParameter {
    private String anchor;
    private Integer limit;
    private Integer context;
    private String searchFilter;
}
