package br.com.multidatasources.multidatasources.config.datasource;

public enum DataSourceType {

    READ_ONLY("Slave-DB"),
    READ_WRITE("Master-DB");

    private final String poolName;

    DataSourceType(String poolName) {
        this.poolName = poolName;
    }

    public String getPoolName() {
        return this.poolName;
    }

}
