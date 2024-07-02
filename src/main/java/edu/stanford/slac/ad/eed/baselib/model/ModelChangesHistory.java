package edu.stanford.slac.ad.eed.baselib.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.ldap.odm.annotations.Id;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
public class ModelChangesHistory {
    @Id
    private String id;
    private String modelId;
    @Builder.Default
    private List<ModelChange> changes = new ArrayList<>();
    /**
     * The date and time when the activity was created.
     * This field is automatically populated with the creation date and time, using @CreatedDate annotation.
     */
    @CreatedDate
    private LocalDateTime createdDate;
    /**
     * The identifier of the user who created the activity.
     * This field stores the ID of the user who initially created the activity, using @CreatedBy annotation.
     */
    @CreatedBy
    private String createdBy;
}
