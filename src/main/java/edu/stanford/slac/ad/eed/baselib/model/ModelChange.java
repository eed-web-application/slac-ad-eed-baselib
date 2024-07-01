package edu.stanford.slac.ad.eed.baselib.model;

import lombok.*;

/**
 * The changes made to a model
 */

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ModelChange {
    private Class<?> fieldType;
    private String fieldName;
    private Object oldValue;
    private Object newValue;
}
