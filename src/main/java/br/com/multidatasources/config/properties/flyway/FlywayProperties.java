package br.com.multidatasources.config.properties.flyway;

import java.util.List;

public class FlywayProperties {

    private String schemaName;
    private boolean baselineOnMigrate;
    private List<String> locations;

    public FlywayProperties() { }

    public FlywayProperties(final String schemaName, final boolean baselineOnMigrate, final List<String> locations) {
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
        return locations.toArray(new String[0]);
    }

    public void setLocations(final List<String> locations) {
        this.locations = locations;
    }

}
