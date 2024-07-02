package edu.stanford.slac.ad.eed.baselib.api.v1.mapper;

import edu.stanford.slac.ad.eed.baselib.api.v1.dto.ModelChangeDTO;
import edu.stanford.slac.ad.eed.baselib.api.v1.dto.ModelChangesHistoryDTO;
import edu.stanford.slac.ad.eed.baselib.model.ModelChange;
import edu.stanford.slac.ad.eed.baselib.model.ModelChangesHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring"
)
public abstract class ModelChangeMapper {
    @Mapping(target = "changes", qualifiedByName = "toModelChangeDTO")
    abstract public ModelChangesHistoryDTO toDTO(ModelChangesHistory modelChangesHistory);

    @Named("toModelChangeDTO")
    public List<ModelChangeDTO> toModelChangeDTO(List<ModelChange> modelChanges) {
        List<ModelChangeDTO> result = new ArrayList<>();
        if (modelChanges == null) return result;
        return modelChanges.stream()
                .map(
                        modelChange -> ModelChangeDTO
                                .builder()
                                .fieldName(modelChange.getFieldName())
                                .oldValue(modelChange.getOldValue()!=null?modelChange.getOldValue().toString():null)
                                .newValue(modelChange.getNewValue()!=null?modelChange.getNewValue().toString():null)
                                .build()
                )
                .toList();
    }
}
