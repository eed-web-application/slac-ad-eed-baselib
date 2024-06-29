package edu.stanford.slac.ad.eed.baselib.model;

import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation using to capture changes of the filed fo a model
 * every time a model is saved a ModelHistory object is created with all the
 * changes from all annotated fields
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CaptureChanges {
}
