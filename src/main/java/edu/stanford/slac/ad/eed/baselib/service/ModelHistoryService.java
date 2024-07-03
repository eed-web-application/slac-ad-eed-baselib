package edu.stanford.slac.ad.eed.baselib.service;

import edu.stanford.slac.ad.eed.baselib.api.v1.dto.ModelChangesHistoryDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
/**
 * Service class for model history operations
 */
public abstract class ModelHistoryService {
    /**
     * Find the changes for a model id
     * @param modelId The identifier of the model
     * @return The list of changes for the model
     */
    abstract public List<ModelChangesHistoryDTO> findChangesByModelId(String modelId);

    /**
     * Find the changes for a model id
     * @param modelClazz The class of the model
     * @param modelId The identifier of the model
     * @return The list of changes for the model
     */
    abstract public <T> List<T> findModelChangesByModelId(Class<T> modelClazz, String modelId);
}

