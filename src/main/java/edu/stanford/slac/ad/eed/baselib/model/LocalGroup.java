package edu.stanford.slac.ad.eed.baselib.model;

import lombok.*;
import org.springframework.data.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LocalGroup {
    /**
     * The identifier of the activity.
     * This field is annotated with @Id to indicate that it is the primary identifier of the activity.
     */
    @Id
    String id;
    /**
     * The name of the activity.
     */
    String description;
    /**
     * The list of members of the activity.
     */
    List<String> members;
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

    /**
     * The date and time when the activity was last modified.
     * This field is automatically updated with the date and time of the last modification, using @LastModifiedDate annotation.
     */
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    /**
     * The identifier of the user who last modified the activity.
     * This field stores the ID of the user who made the last modification to the activity, using @LastModifiedBy annotation.
     */
    @LastModifiedBy
    private String lastModifiedBy;
}
