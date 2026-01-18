package performance;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SortTimer {

    private List<PerformanceMeasurement> measurements;

    public SortTimer() {
        this.measurements = new ArrayList<>();
    }

    public PerformanceMeasurement measureSort(String algorithmName, double[] data, Consumer<double[]> sortFunction) {
        double[] arrCopy = data.clone();

        long startTime = System.nanoTime();
        sortFunction.accept(arrCopy);
        long endTime = System.nanoTime();

        long executionTime = endTime - startTime;
        PerformanceMeasurement measurement = new PerformanceMeasurement(algorithmName, executionTime);
        measurements.add(measurement);

        return measurement;
    }

    public PerformanceMeasurement getFastest() {
        if (measurements.isEmpty()) {
            return null;
        }

        PerformanceMeasurement fastest = measurements.get(0);
        for (PerformanceMeasurement measurement : measurements) {
            if (measurement.getExecutionTimeNanos() < fastest.getExecutionTimeNanos()) {
                fastest = measurement;
            }
        }

        return fastest;
    }

    public List<PerformanceMeasurement> getAllMeasurements() {
        return new ArrayList<>(measurements);
    }

    public void clear() {
        measurements.clear();
    }

    public String generateReport() {
        if (measurements.isEmpty()) {
            return "No measurements recorded.";
        }

        StringBuilder report = new StringBuilder();
        report.append("Sorting Performance Results\n\n");

        for (PerformanceMeasurement measurement : measurements) {
            report.append("- ").append(measurement.toString()).append("\n");
        }

        PerformanceMeasurement fastest = getFastest();
        report.append("\n");
        report.append("FASTEST: ").append(fastest.getAlgorithmName());
        report.append(" (").append(fastest.getFormattedTime()).append(")\n");

        return report.toString();
    }
}