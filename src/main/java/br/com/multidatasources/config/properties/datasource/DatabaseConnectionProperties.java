package br.com.multidatasources.config.properties.datasource;

public class DatabaseConnectionProperties {

    private String url;
    private String username;
    private String password;

    public DatabaseConnectionProperties() { }

    public DatabaseConnectionProperties(final String url, final String username, final String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

}
