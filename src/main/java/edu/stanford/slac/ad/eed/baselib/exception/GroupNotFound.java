package edu.stanford.slac.ad.eed.baselib.exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The group has not been found")
public class GroupNotFound extends ControllerLogicException {
    @Builder(builderMethodName = "groupNotFound")
    public GroupNotFound(Integer errorCode, String groupId, String errorDomain) {
        super(errorCode, "The group with id '%s' has not been found".formatted(groupId), errorDomain);
    }
}