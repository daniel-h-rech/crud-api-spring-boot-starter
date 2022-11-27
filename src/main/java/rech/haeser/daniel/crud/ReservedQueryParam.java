package rech.haeser.daniel.crud;


import com.google.common.base.CaseFormat;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

enum ReservedQueryParam {
    ORDER_BY,
    PAGE_INDEX,
    PAGE_SIZE,
    ;

    String getParamName() {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());
    }

    static Set<String> getAllParamNames() {
        return Arrays.stream(values())
                .map(ReservedQueryParam::getParamName)
                .collect(Collectors.toSet());
    }
}
