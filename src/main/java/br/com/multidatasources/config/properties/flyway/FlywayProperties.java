package br.com.multidatasources.config.properties.flyway;

public class FlywayProperties {

    private String schemaName;
    private boolean baselineOnMigrate;
    private String[] locations;

    public FlywayProperties() { }

    public FlywayProperties(final String schemaName, final boolean baselineOnMigrate, final String[] locations) {
        this.schemaName = schemaName;
        this.baselineOnMigrate = baselineOnMigrate;
        this.locations = locations;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(final String schemaName) {
        this.schemaName = schemaName;
    }

    public boolean isBaselineOnMigrate() {
        return baselineOnMigrate;
    }

    public void setBaselineOnMigrate(final boolean baselineOnMigrate) {
        this.baselineOnMigrate = baselineOnMigrate;
    }

    public String[] getLocations() {
        return locations;
    }

    public void setLocations(final String[] locations) {
        this.locations = locations;
    }

}
