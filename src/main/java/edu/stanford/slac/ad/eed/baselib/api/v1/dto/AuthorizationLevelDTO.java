package edu.stanford.slac.ad.eed.baselib.api.v1.dto;

public enum AuthorizationLevelDTO {
    Read,
    // can only read and write on the resource data
    Write,
    // can read, write and administer the resource data
    Admin;
}
