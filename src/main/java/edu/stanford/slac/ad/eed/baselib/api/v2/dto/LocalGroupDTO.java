package edu.stanford.slac.ad.eed.baselib.api.v2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import edu.stanford.slac.ad.eed.baselib.api.v1.dto.PersonDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Local group information")
public record LocalGroupDTO(
        @Schema(description = "The id of the local group")
        String id,
        @Schema(description = "The name of the local group")
        String name,
        @Schema(description = "The description of the local group")
        String description,
        @Schema(description = "The list of members of the local group")
        List<PersonDTO> members
){}
