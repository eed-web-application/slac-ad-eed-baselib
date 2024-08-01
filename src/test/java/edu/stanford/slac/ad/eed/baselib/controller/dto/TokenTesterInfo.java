package edu.stanford.slac.ad.eed.baselib.controller.dto;

import edu.stanford.slac.ad.eed.baselib.api.v1.dto.AuthorizationDTO;

import java.util.List;

public class TokenTesterInfo {
    public boolean isAuthenticated;
    public boolean isImpersonating;
    public String principal;
    public String credential;
    public String sourcePrincipal;
    public List<AuthorizationDTO> authorizationDTOS;
}
