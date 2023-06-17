package br.com.multidatasources.api.v1.billionaire.input;

import jakarta.validation.constraints.NotBlank;

public record BillionaireInput(
    @NotBlank(message = "First name is required")
    String firstName,

    @NotBlank(message = "Last name is required")
    String lastName,

    @NotBlank(message = "Career is required")
    String career
) {

    public static BillionaireInput with(
        final String firstName,
        final String lastName,
        final String career
    ) {
        return new BillionaireInput(
            firstName,
            lastName,
            career
        );
    }

}
