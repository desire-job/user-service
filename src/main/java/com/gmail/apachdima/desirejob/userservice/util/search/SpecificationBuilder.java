package com.gmail.apachdima.desirejob.userservice.util.search;

import com.gmail.apachdima.desirejob.userservice.model.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SpecificationBuilder {

    private final List<SearchCriteria> searchCriteria = new ArrayList<>();

    public final void with(SearchCriteria searchCriteria) {
        this.searchCriteria.add(searchCriteria);
    }

    public Specification<User> build() {
        if (searchCriteria.size() == 0) {
            return null;
        }
        Specification<User> result = new UserSpecification(searchCriteria.get(0));
        for (int i = 1; i < searchCriteria.size(); i++) {
            SearchCriteria criteria = searchCriteria.get(i);
            result = SearchOption.AND.equals(searchCriteria.get(i-1).getSearchOption())
                ? Specification.where(result).and(new UserSpecification(criteria))
                : Specification.where(result).or(new UserSpecification(criteria));
        }
        return result;
    }
}
