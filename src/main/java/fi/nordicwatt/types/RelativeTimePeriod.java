package fi.nordicwatt.types;

/**
 * Stores the relative time periods supported by the application.
 * Override holds the logic for calculating the from date.
 * 
 * @author Antti Hakkarainen
 */
import java.time.LocalDate;

public enum RelativeTimePeriod {
    TODAY("Today") {
        @Override
        public LocalDate getFromDate() {
            return LocalDate.now();
        }
    },
    LAST_3_DAYS("Last 3 days") {
        @Override
        public LocalDate getFromDate() {
            return LocalDate.now().minusDays(3);
        }
    },
    LAST_7_DAYS("Last 7 days") {
        @Override
        public LocalDate getFromDate() {
            return LocalDate.now().minusDays(7);
        }
    },
    LAST_MONTH("Last month") {
        @Override
        public LocalDate getFromDate() {
            return LocalDate.now().minusMonths(1);
        }
    },
    LAST_YEAR("Last year") {
        @Override
        public LocalDate getFromDate() {
            return LocalDate.now().minusYears(1);
        }
    };

    private final String displayValue;

    RelativeTimePeriod(String displayValue) {
        this.displayValue = displayValue;
    }

    @Override
    public String toString() {
        return displayValue;
    }

    public abstract LocalDate getFromDate();

    public LocalDate getToDate() {
        return LocalDate.now();
    }
}
