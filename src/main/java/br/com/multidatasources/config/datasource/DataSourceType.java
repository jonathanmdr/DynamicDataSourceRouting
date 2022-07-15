package br.com.multidatasources.config.datasource;

public enum DataSourceType {

    READ_ONLY("Slave-DB") {
        @Override
        public int getMinimumIdle() {
            return Runtime.getRuntime().availableProcessors();
        }

        @Override
        public int getMaximumPoolSize() {
            return Runtime.getRuntime().availableProcessors() * 4;
        }

        @Override
        public long getConnectionTimeout() {
            return 250;
        }

        @Override
        public long getMaxLifetime() {
            return 600000;
        }
    },
    READ_WRITE("Master-DB") {
        @Override
        public int getMinimumIdle() {
            return Runtime.getRuntime().availableProcessors();
        }

        @Override
        public int getMaximumPoolSize() {
            return Runtime.getRuntime().availableProcessors() * 2;
        }

        @Override
        public long getConnectionTimeout() {
            return 250;
        }

        @Override
        public long getMaxLifetime() {
            return 600000;
        }
    };

    private final String poolName;

    DataSourceType(final String poolName) {
        this.poolName = poolName;
    }

    public String getPoolName() {
        return this.poolName;
    }

    public abstract int getMinimumIdle();

    public abstract int getMaximumPoolSize();

    public abstract long getConnectionTimeout();

    public abstract long getMaxLifetime();

}
