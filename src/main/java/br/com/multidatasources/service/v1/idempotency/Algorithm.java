package br.com.multidatasources.service.v1.idempotency;

public enum Algorithm {

    SHA_256("SHA-256");

    private final String value;

    Algorithm(final String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

}
