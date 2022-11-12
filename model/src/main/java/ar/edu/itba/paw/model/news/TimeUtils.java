package ar.edu.itba.paw.model.news;

import java.time.*;

public final class TimeUtils {

    private TimeUtils() {
    }

    static Amount calculateTimeAgoWithPeriodAndDuration(final LocalDateTime pastTime) {
        final Period period = Period.between(pastTime.toLocalDate(), LocalDate.from(LocalDateTime.now()));
        final Duration duration = Duration.between(pastTime, LocalDateTime.now());
        long qty;
        if ((qty = period.getYears()) != 0) {
            return new Amount(Unit.YEARS, qty);
        } else if ((qty = period.getMonths()) != 0) {
            return new Amount(Unit.MONTHS, qty);
        } else if ((qty = period.getDays()) != 0) {
            return new Amount(Unit.DAYS, qty);
        } else if ((qty = duration.toHours()) != 0) {
            return new Amount(Unit.HOURS, qty);
        } else if ((qty = duration.toMinutes()) != 0) {
            return new Amount(Unit.MINUTES, qty);
        } else if ((qty = duration.getSeconds()) != 0) {
            return new Amount(Unit.SECONDS, qty);
        } else {
            return new Amount(Unit.SECONDS, 0);
        }
    }

    public static class Amount {
        private final Unit unit;
        private final Long qty;

        private Amount(final Unit unit, long qty) {
            this.unit = unit;
            this.qty = qty;
        }

        public Long getQty() {
            return qty;
        }

        public Unit getUnit() {
            return unit;
        }

        public String getInterCode() {
            return unit.getInterCode();
        }
    }

    public enum Unit {
        YEARS("unit.years"),
        MONTHS("unit.months"),
        DAYS("unit.days"),
        HOURS("unit.hours"),
        MINUTES("unit.minutes"),
        SECONDS("unit.seconds"),
        ;

        private final String interCode;

        Unit(String interCode) {
            this.interCode = interCode;
        }

        public String getInterCode() {
            return interCode;
        }
    }
}
