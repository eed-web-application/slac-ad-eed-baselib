package edu.stanford.slac.ad.eed.baselib.model;

import lombok.*;
import org.springframework.data.annotation.*;

import java.time.LocalDateTime;

/**
 * Define the authorizations level on a specific resource
 *
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Authorization {
    /**
     * all authorizations typeAuthService
     */
    @Getter
    public enum Type {
        // can only read on the resource data
        Read(0),
        // can only read and write on the resource data
        Write(1),
        // can read, write and administer the resource data
        Admin(2);
        private final int value;

        Type(int value) {
            this.value = value;
        }
        public static Type fromValue(int value) {
            for (Type myEnum : Type.values()) {
                if (myEnum.value == value) {
                    return myEnum;
                }
            }
            throw new IllegalArgumentException("Invalid value: " + value);
        }
    }

    @Id
    private String id;
    /**
     * The authorizationType is a general string value
     * that express the entitlement to take an action
     * on the specific resource
     */
    private Integer authorizationType;
    /**
     * Is the type of the resource to which the
     * authorizations belong
     * ex: /logbook/id
     * ex: /logbook/id/entry/id
     */
    private String resource;
    /**
     * the value identify the owner of the authorizations
     */
    private String owner;

    /**
     * Specify the type of the authorization form @{@link AuthorizationOwnerType}
     */
    private AuthorizationOwnerType ownerType;

    // default model information
    @CreatedDate
    private LocalDateTime creationDate;
    @CreatedBy
    private String creationBy;
    @LastModifiedDate
    private LocalDateTime modificationDate;
    @LastModifiedBy
    private String modifiedBy;
}
