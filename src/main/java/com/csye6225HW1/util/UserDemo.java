package com.csye6225HW1.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class UserDemo {


        private Long id;
        private String username;

        @JsonProperty("first_name")
        private String firstName;
        @JsonProperty("last_name")
        private String lastName;

        @JsonProperty("account_created")
        private String accountCreated;

        @JsonProperty("account_updated")
        private String accountUpdated;


}
