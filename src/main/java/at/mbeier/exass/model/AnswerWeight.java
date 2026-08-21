package at.mbeier.exass.model;

public enum AnswerWeight {
    P100("100", 100.0),
    P90("90", 90.0),
    P83("83.33333", 100.0*5.0/6.0),
    P80("80", 80.0),
    P75("75", 75.0),
    P70("70", 70.0),
    P66("66.66667", 100.0*2.0/3.0),
    P60("60", 60.0),
    P50("50", 50.0),
    P40("40", 40.0),
    P33("33.33333", 100.0/3.0),
    P30("30", 30.0),
    P25("25", 25.0),
    P20("20", 20.0),
    P16("16.66667", 100.0/6.0),
    P14("14.28571", 100.0/7.0),
    P12("12.5", 12.5),
    P11("11.11111", 100.0/9.0),
    P10("10", 10.0),
    P5("5",5.0),
    P0("", 0.0);

    private final String representation;
    private final double value;

    AnswerWeight(String representation, double value) {
        this.representation = representation;
        this.value = value;
    }


    public double getValue() {
        return this.value;
    }

    public String getRepresentation() {
        return this.representation;
    }

    public static AnswerWeight calculate(int total, int amountCorrect) {
        return AnswerWeight.parse((double) amountCorrect / (double) total * 100.0);
    }

    public static AnswerWeight parse(double value) {
        for (AnswerWeight weight : AnswerWeight.values()) {
            if (weight.value == value) {
                return weight;
            }
        }
        return null;
    }
}
