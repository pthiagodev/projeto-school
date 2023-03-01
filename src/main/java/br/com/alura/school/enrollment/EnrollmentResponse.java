package br.com.alura.school.enrollment;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Positive;

public class EnrollmentResponse {

    @JsonProperty
    private final String email;

    @JsonProperty
    private final Long enrollments;

    EnrollmentResponse(String email, Long enrollments) {
        this.email = email;
        this.enrollments = enrollments;
    }
}
