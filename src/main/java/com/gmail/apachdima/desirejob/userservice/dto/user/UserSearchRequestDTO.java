package com.gmail.apachdima.desirejob.userservice.dto.user;

import com.gmail.apachdima.desirejob.userservice.util.search.SearchCriteria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserSearchRequestDTO {

    private List<SearchCriteria> searchCriteria;
}
