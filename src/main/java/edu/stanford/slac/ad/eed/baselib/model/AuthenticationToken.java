package edu.stanford.slac.ad.eed.baselib.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Is the global application token, is not strictly correlated to a logbook but it can be associated
 * to more than one logbook and can be also set as admin
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationToken {
    // the unique id of the token
    private String id;
    // the token name
    private String name;
    // the token expiration
    private LocalDate expiration;
    // the auto-generated email
    private String email;
    // the auto-generated jwt-token
    private String token;
    @Builder.Default
    // this application managed token and cannot be modified by user or admin
    private Boolean applicationManaged = false;
    // default model information
    @CreatedDate
    private LocalDateTime createdDate;
    @CreatedBy
    private String createdBy;
    @LastModifiedDate
    private LocalDateTime modifiedDate;
    @LastModifiedBy
    private String modifiedBy;
}
