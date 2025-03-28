package at.mbeier.examassistant.model.questions;

public enum Percentage {

    P100("100%", 100.0),
    P90("90%", 90.0),
    P83_33333("83.33333%", 83.33333),
    P80("80%", 80.0),
    P75("75%", 75.0),
    P70("70%", 70.0),
    P66_66667("66.66667%", 66.66667),
    P60("60%", 60.0),
    P50("50%", 50.0),
    P40("40%", 40.0),
    P33_33333("33.33333%", 33.33333),
    P30("30%", 30.0),
    P25("25%", 25.0),
    P20("20%", 20.0),
    P16_66667("16.66667%", 16.66667),
    P14_28571("14.28571%", 14.28571),
    P12_5("12.5%", 12.5),
    P11_11111("11.11111%", 11.11111),
    P10("10%", 10.0),
    P5("5%", 5.0),
    P0("0%", 0.0),
    M5("-5%", -5.0),
    M10("-10%", -10.0),
    M11_11111("-11.11111%", -11.11111),
    M12_5("-12.5%", -12.5),
    M14_28571("-14.28571%", -14.28571),
    M16_66667("-16.66667%", -16.66667),
    M20("-20%", -20.0),
    M25("-25%", -25.0),
    M30("-30%", -30.0),
    M33_33333("-33.33333%", -33.33333),
    M40("-40%", -40.0),
    M50("-50%", -50.0),
    M60("-60%", -60.0),
    M66_66667("-66.66667%", -66.66667),
    M70("-70%", -70.0),
    M75("-75%", -75.0),
    M80("-80%", -80.0),
    M83_33333("-83.33333%", -83.33333),
    M90("-90%", -90.0),
    M100("-100%", -100.0),;

    private final String representation;
    private final double percentage;

    Percentage(String representation, double percentage) {
        this.representation = representation;
        this.percentage = percentage;
    }

    public String getGIFTValue() {
        if (this.equals(P0)) return "";
        return "%" + this.representation;
    }

    public String getXMLValue() {
        return this.representation.substring(0, this.representation.lastIndexOf("%"));
    }

    public static Percentage getPercentage(String representation) {
        for (Percentage percentage : Percentage.values()) {
            if (percentage.representation.equals(representation)) {
                return percentage;
            }
        }
        return null;
    }

    public static Percentage getPercentage(double percentage) {
        double rounded = Math.round(percentage * 100000.0) / 100000.0;
        for (Percentage p : Percentage.values()) {
            if (p.percentage == rounded) {
                return p;
            }
        }
        return null;
    }

    public static Percentage calculate(int correct, int wrong) {
        double total = correct + wrong;
        return Percentage.getPercentage(correct / total * 100.0);
    }
}
