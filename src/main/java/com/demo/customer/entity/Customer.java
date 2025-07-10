package com.demo.customer.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "Customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @Column(name = "Id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "FirstName", nullable = false, length = 255)
    @JsonProperty("firstName")
    private String firstName;

    @Column(name = "MiddleName", length = 255)
    @JsonProperty("middleName")
    private String middleName;

    @Column(name = "LastName", nullable = false, length = 255)
    @JsonProperty("lastName")
    private String lastName;

    @Column(name = "EmailAddress", nullable = false, unique = true, length = 255)
    @JsonProperty("emailAddress")
    private String emailAddress;

    @Column(name = "PhoneNumber", length = 20)
    @JsonProperty("phoneNumber")
    private String phoneNumber;
}
