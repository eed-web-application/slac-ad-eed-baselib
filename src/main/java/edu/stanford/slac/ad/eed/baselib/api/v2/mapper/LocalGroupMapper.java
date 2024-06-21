package edu.stanford.slac.ad.eed.baselib.api.v2.mapper;

import edu.stanford.slac.ad.eed.baselib.api.v2.dto.LocalGroupDTO;
import edu.stanford.slac.ad.eed.baselib.api.v2.dto.LocalGroupQueryParameterDTO;
import edu.stanford.slac.ad.eed.baselib.api.v2.dto.NewLocalGroupDTO;
import edu.stanford.slac.ad.eed.baselib.api.v2.dto.UpdateLocalGroupDTO;
import edu.stanford.slac.ad.eed.baselib.model.LocalGroup;
import edu.stanford.slac.ad.eed.baselib.model.LocalGroupQueryParameter;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring"
)
public abstract class LocalGroupMapper {
    /**
     * Maps a {@link NewLocalGroupDTO} to a {@link LocalGroup}
     *
     * @param dto The {@link NewLocalGroupDTO} to map
     * @return The mapped {@link LocalGroup}
     */
    public abstract LocalGroup fromDTO(NewLocalGroupDTO dto);

    /**
     * Maps a {@link UpdateLocalGroupDTO} to a {@link LocalGroup}
     *
     * @param dto The {@link UpdateLocalGroupDTO} to map
     * @return The mapped {@link LocalGroup}
     */
    public abstract LocalGroup updateModel(UpdateLocalGroupDTO dto, @MappingTarget LocalGroup localGroup);

    /**
     * Maps a {@link LocalGroup} to a {@link LocalGroupDTO}
     *
     * @param localGroup The {@link LocalGroup} to map
     * @return The mapped {@link LocalGroupDTO}
     */
    public abstract LocalGroupDTO toDTO(LocalGroup localGroup);

    /**
     * Maps a {@link LocalGroupQueryParameterDTO} to a {@link LocalGroupQueryParameter}
     *
     * @param localGroup The {@link LocalGroupQueryParameterDTO} to map
     * @return The mapped {@link LocalGroupQueryParameter}
     */
    public abstract LocalGroupQueryParameter toQuery(LocalGroupQueryParameterDTO localGroup);
}
