package br.com.multidatasources.multidatasources.controller.dto;

import javax.validation.constraints.NotBlank;

public class BillionairesInputDto {

    @NotBlank(message = "First name is mandatory")
    public String firstName;
    public String lastName;
    public String career;

    public BillionairesInputDto() { }

    public BillionairesInputDto(String firstName, String lastName, String career) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.career = career;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

}
