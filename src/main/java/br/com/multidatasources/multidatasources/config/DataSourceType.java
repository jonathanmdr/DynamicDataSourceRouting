package br.com.multidatasources.multidatasources.config;

public enum DataSourceType {
    READ_ONLY("Slave-DB"),
    READ_WRITE("Master-DB");

    private final String poolName;

    private DataSourceType(String poolName) {
        this.poolName = poolName;
    }

    public String getPoolName() {
        return this.poolName;
    }
}
