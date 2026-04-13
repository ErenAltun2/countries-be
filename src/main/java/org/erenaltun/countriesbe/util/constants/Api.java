package org.erenaltun.countriesbe.util.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Api {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Country{

        public static final String BASE_URL="/country";

        public static final String INSERT_ALL="/insertAll";

        public static final String INSERT_COUNTRY="/insert";

        public static final String GET_COUNTRY="/getcountry/{code}";

        public static final String DELETE_COUNTRY="/deletecountry/{code}";

        public static final String PUT_COUNTRY="/putcountry/{code}/{name}";
    }
}
