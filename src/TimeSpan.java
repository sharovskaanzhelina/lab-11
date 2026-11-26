import exceptions.IllegalInputTimeSpanException;
import exceptions.IllegalScaleFactorException;
import exceptions.NegativeTimeSpanException;

public class TimeSpan {
    private int hours;
    private int minutes;

    public TimeSpan() {
        this.hours = 0;
        this.minutes = 0;
    }

    public TimeSpan(int minutes) {
        if (minutes < 0) {
            throw new IllegalInputTimeSpanException("Кількість хвилин не може бути від'ємною: " + minutes);
        }
        this.hours = minutes / 60;
        this.minutes = minutes % 60;
    }

    public TimeSpan(int hours, int minutes) {
        validateInput(hours, minutes);
        this.hours = hours;
        this.minutes = minutes;
    }

    public TimeSpan(TimeSpan span) {
        if (span == null) {
            throw new IllegalInputTimeSpanException("Інтервал часу не може бути null");
        }
        this.hours = span.hours;
        this.minutes = span.minutes;
    }

    private void validateInput(int hours, int minutes) {
        if (hours < 0 || minutes < 0 || minutes > 59) {
            throw new IllegalInputTimeSpanException(
                    "Некоректний інтервал часу: години = " + hours + ", хвилини = " + minutes);
        }
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void add(int hours, int minutes) {
        validateInput(hours, minutes);
        int totalMinutes = getTotalMinutes() + hours * 60 + minutes;
        this.hours = totalMinutes / 60;
        this.minutes = totalMinutes % 60;
    }

    public void add(int minutes) {
        if (minutes < 0) {
            throw new IllegalInputTimeSpanException("Кількість хвилин не може бути від'ємною: " + minutes);
        }
        int totalMinutes = getTotalMinutes() + minutes;
        this.hours = totalMinutes / 60;
        this.minutes = totalMinutes % 60;
    }

    public void add(TimeSpan span) {
        if (span == null) {
            throw new IllegalInputTimeSpanException("Інтервал часу не може бути null");
        }
        add(span.hours, span.minutes);
    }

    public double getTotalHours() {
        return hours + minutes / 60.0;
    }

    public int getTotalMinutes() {
        return hours * 60 + minutes;
    }

    public void subtract(TimeSpan span) {
        if (span == null) {
            throw new IllegalInputTimeSpanException("Інтервал часу не може бути null");
        }
        int resultMinutes = getTotalMinutes() - span.getTotalMinutes();
        if (resultMinutes < 0) {
            throw new NegativeTimeSpanException("Результат віднімання інтервалів часу не може бути від'ємним");
        }
        this.hours = resultMinutes / 60;
        this.minutes = resultMinutes % 60;
    }

    public void scale(int factor) {
        if (factor <= 0) {
            throw new IllegalScaleFactorException("Множник має бути більше 0, отримано: " + factor);
        }
        int totalMinutes = getTotalMinutes() * factor;
        this.hours = totalMinutes / 60;
        this.minutes = totalMinutes % 60;
    }

    @Override
    public String toString() {
        return hours + "h " + minutes + "m";
    }
}