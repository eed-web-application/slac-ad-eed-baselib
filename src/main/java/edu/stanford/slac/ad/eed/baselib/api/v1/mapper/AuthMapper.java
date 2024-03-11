package edu.stanford.slac.ad.eed.baselib.api.v1.mapper;


import edu.stanford.slac.ad.eed.baselib.api.v1.dto.AuthorizationDTO;
import edu.stanford.slac.ad.eed.baselib.api.v1.dto.AuthorizationTypeDTO;
import edu.stanford.slac.ad.eed.baselib.api.v1.dto.GroupDTO;
import edu.stanford.slac.ad.eed.baselib.api.v1.dto.NewAuthenticationTokenDTO;
import edu.stanford.slac.ad.eed.baselib.config.AppProperties;
import edu.stanford.slac.ad.eed.baselib.api.v1.dto.*;
import edu.stanford.slac.ad.eed.baselib.model.AuthenticationToken;
import edu.stanford.slac.ad.eed.baselib.model.Authorization;
import edu.stanford.slac.ad.eed.baselib.model.Group;
import edu.stanford.slac.ad.eed.baselib.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring"
)
public abstract class AuthMapper {
    static public final String EMAIL_FORMAT = "%s@%s";
    static public final String EMAIL_FORMAT_CUSTOM_APP_DOMAIN = "%s@%s.%s";
    @Autowired
    protected AppProperties appProperties;
    public abstract PersonDTO fromModel(Person p);
    public abstract GroupDTO fromModel(Group g);
    @Mapping(target = "authorizationType", expression = "java(AuthorizationTypeDTO.valueOf(Authorization.Type.fromValue(a.getAuthorizationType()).name()))")
    public abstract AuthorizationDTO fromModel(Authorization a);
    @Mapping(target = "authorizationType", expression = "java(Authorization.Type.valueOf(a.authorizationType().name()).getValue())")
    public abstract Authorization toModel(AuthorizationDTO a);
    @Mapping(target = "authorizationType", expression = "java(Authorization.Type.valueOf(a.authorizationType().name()).getValue())")
    public abstract Authorization toModel(NewAuthorizationDTO a);
    public abstract List<Authorization> toModel(List<AuthorizationDTO> a);
    @Mapping(target = "email", expression = "java(EMAIL_FORMAT.formatted(a.name(), appProperties.getAppEmailPostfix()))")
    public abstract AuthenticationToken toModelAuthenticationToken(AuthenticationTokenDTO a);
    @Mapping(target = "email", expression = "java(EMAIL_FORMAT.formatted(a.name(), appProperties.getAppEmailPostfix()))")
    public abstract AuthenticationToken toModelApplicationToken(NewAuthenticationTokenDTO a);
    @Mapping(target = "email", expression = "java(EMAIL_FORMAT_CUSTOM_APP_DOMAIN.formatted(a.name(), prefixDomain, appProperties.getAppEmailPostfix()))")
    public abstract AuthenticationToken toModelApplicationToken(NewAuthenticationTokenDTO a, String prefixDomain);
    @Mapping(target = "email", expression = "java(EMAIL_FORMAT.formatted(a.name(), appProperties.getAuthenticationTokenDomain()))")
    public abstract AuthenticationToken toModelGlobalToken(AuthenticationTokenDTO a);
    @Mapping(target = "email", expression = "java(EMAIL_FORMAT.formatted(a.name(), appProperties.getAuthenticationTokenDomain()))")
    public abstract AuthenticationToken toModelGlobalToken(NewAuthenticationTokenDTO a);
    public abstract AuthenticationTokenDTO toTokenDTO(AuthenticationToken a);
    public abstract Authorization.Type toModel(AuthorizationTypeDTO type);
}