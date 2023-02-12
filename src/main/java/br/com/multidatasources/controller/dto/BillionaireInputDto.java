package br.com.multidatasources.controller.dto;

import jakarta.validation.constraints.NotBlank;

public class BillionaireInputDto {

    @NotBlank(message = "First name is mandatory")
    public String firstName;

    @NotBlank(message = "Last name is mandatory")
    public String lastName;

    @NotBlank(message = "Career is mandatory")
    public String career;

    public BillionaireInputDto() { }

    public BillionaireInputDto(final String firstName, final String lastName, final String career) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.career = career;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(final String career) {
        this.career = career;
    }

}
