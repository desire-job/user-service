package com.gmail.apachdima.desirejob.userservice.util.search;

import com.gmail.apachdima.desirejob.userservice.model.Role;
import com.gmail.apachdima.desirejob.userservice.model.User;
import com.gmail.apachdima.desirejob.userservice.model.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

@RequiredArgsConstructor
public class UserSpecification implements Specification<User> {

    private final SearchCriteria searchCriteria;

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        String searchValue = searchCriteria.getValue().trim();
        if (SearchOperation.EQUAL.equals(searchCriteria.getOperation())) {
            return searchCriteria.getSearchKey().equals("role")
                ? builder.equal(roleJoin(root).get(searchCriteria.getSearchKey()), UserRole.valueOf(searchValue))
                : builder.equal(root.get(searchCriteria.getSearchKey()), searchValue);
        }
        throw new UnsupportedOperationException(String.valueOf(searchCriteria.getOperation()));
    }

    private Join<User, Role> roleJoin (Root<User> root) {
        return root.join("roles");
    }
}
