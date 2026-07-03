public class Main {
    public static void main(String[] args) {
        // Create a new meter with id "MTR-100" and 200 UGX of starting credit
        SmartMeter meter = new SmartMeter("MTR-100", 200.0);
        System.out.println(meter); // shows balance=200.00, valveOpen=true

        // Consume 2 litres -> costs 2 * 50 = 100 UGX
        meter.recordConsumption(2);
        System.out.println(meter); // balance should now be 100.00, valve still open

        // Consume 3 more litres -> would cost 150 UGX, but only 100 UGX left
        // -> balance clamps to 0, valve closes automatically
        meter.recordConsumption(3);
        System.out.println(meter); // balance=0.00, valveOpen=false

        // Load a fresh token of 1000 UGX -> valve reopens automatically
        meter.loadToken(1000);
        System.out.println(meter); // balance=1000.00, valveOpen=true
    }
}