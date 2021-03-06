package br.com.multidatasources.model.factory;

import br.com.multidatasources.model.Billionaire;

public class BillionaireBuilder {

    private final Billionaire billionaire;

    private BillionaireBuilder(final Billionaire billionaire) {
        this.billionaire = billionaire;
    }

    public static BillionaireBuilder builder() {
        return new BillionaireBuilder(new Billionaire());
    }

    public BillionaireBuilder id(Long id) {
        this.billionaire.setId(id);
        return this;
    }

    public BillionaireBuilder firstName(String firstName) {
        this.billionaire.setFirstName(firstName);
        return this;
    }

    public BillionaireBuilder lastName(String lastName) {
        this.billionaire.setLastName(lastName);
        return this;
    }

    public BillionaireBuilder career(String career) {
        this.billionaire.setCareer(career);
        return this;
    }

    public Billionaire build() {
        return this.billionaire;
    }

}
