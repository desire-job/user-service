package com.gmail.apachdima.desirejob.userservice.util.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SearchCriteria {

    private String searchKey;
    private String value;
    private SearchOperation operation;
    private SearchOption searchOption;
}
