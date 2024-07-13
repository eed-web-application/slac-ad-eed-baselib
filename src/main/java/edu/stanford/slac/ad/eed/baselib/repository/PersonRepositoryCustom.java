package edu.stanford.slac.ad.eed.baselib.repository;

import edu.stanford.slac.ad.eed.baselib.model.Person;
import edu.stanford.slac.ad.eed.baselib.model.PersonQueryParameter;

import java.util.List;

public interface PersonRepositoryCustom {
    List<Person> findAll(PersonQueryParameter personQueryParameter);
}
