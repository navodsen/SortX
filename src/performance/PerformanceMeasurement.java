package performance;

public class PerformanceMeasurement {

    private String algorithmName;
    private long executionTimeNanos;
    private double executionTimeMillis;

    public PerformanceMeasurement(String algorithmName, long executionTimeNanos) {
        this.algorithmName = algorithmName;
        this.executionTimeNanos = executionTimeNanos;
        this.executionTimeMillis = executionTimeNanos / 1_000_000.0;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public long getExecutionTimeNanos() {
        return executionTimeNanos;
    }

    public double getExecutionTimeMillis() {
        return executionTimeMillis;
    }

    public String getFormattedTime() {
        if (executionTimeMillis < 1000.0) {
            return String.format("%.3f ms", executionTimeMillis);
        } else {
            return String.format("%.3f s", executionTimeMillis / 1000.0);
        }
    }

    @Override
    public String toString() {
        return algorithmName + ": " + getFormattedTime();
    }
}