package br.com.multidatasources.api.v1.billionaire.output;

public record BillionaireOutput(
    Long id,
    String firstName,
    String lastName,
    String career
) {

    public static BillionaireOutput with(
        final Long id,
        final String firstName,
        final String lastName,
        final String career
    ) {
        return new BillionaireOutput(
            id,
            firstName,
            lastName,
            career
        );
    }

}
