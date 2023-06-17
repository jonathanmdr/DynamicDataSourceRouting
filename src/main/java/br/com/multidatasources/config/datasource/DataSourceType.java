package br.com.multidatasources.config.datasource;

public enum DataSourceType {

    READ_ONLY("Replica-DB") {
        @Override
        public int minimumIdle() {
            return Runtime.getRuntime().availableProcessors();
        }

        @Override
        public int maximumPoolSize() {
            return Runtime.getRuntime().availableProcessors() * 4;
        }

        @Override
        public long connectionTimeout() {
            return 250;
        }

        @Override
        public long idleTimeout() {
            return 600000;
        }

        @Override
        public long maxLifetime() {
            return 800000;
        }
    },
    READ_WRITE("Master-DB") {
        @Override
        public int minimumIdle() {
            return Runtime.getRuntime().availableProcessors();
        }

        @Override
        public int maximumPoolSize() {
            return Runtime.getRuntime().availableProcessors() * 2;
        }

        @Override
        public long connectionTimeout() {
            return 250;
        }

        @Override
        public long idleTimeout() {
            return 600000;
        }

        @Override
        public long maxLifetime() {
            return 800000;
        }
    };

    private final String poolName;

    DataSourceType(final String poolName) {
        this.poolName = poolName;
    }

    public String poolName() {
        return this.poolName;
    }

    public abstract int minimumIdle();

    public abstract int maximumPoolSize();

    public abstract long connectionTimeout();

    public abstract long idleTimeout();

    public abstract long maxLifetime();

}
