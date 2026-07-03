/**
 * SmartMeter represents a single prepaid smart water meter in the
 * AquaSmart platform. It holds the credit balance and valve state,
 * and is the single point of control for all credit/consumption
 * changes (encapsulation - see explanation in part d).
 */
public class SmartMeter {

    private static final double COST_PER_LITRE = 50.0; // UGX

    private String meterId;
    private double creditBalance;
    private boolean valveOpen;

    /**
     * Constructor: sets the meter id and opening credit balance,
     * and starts the meter with the valve open.
     */
    public SmartMeter(String meterId, double openingCreditBalance) {
        this.meterId = meterId;
        this.creditBalance = openingCreditBalance;
        this.valveOpen = true;
    }

    /**
     * Adds credit to the meter (a mobile-money token has been loaded).
     * If the valve was closed (because credit had run out), it is
     * automatically re-opened.
     *
     * @param amount the value of the token loaded, in UGX
     * @return the new credit balance after the token is applied
     */
    public double loadToken(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Token amount must be positive.");
        }
        creditBalance += amount;
        if (!valveOpen) {
            valveOpen = true;
        }
        return creditBalance;
    }

    /**
     * Records a consumption event of the given number of litres.
     * Each litre costs UGX 50, deducted from the credit balance.
     * If the balance reaches or falls below zero, it is clamped to
     * zero and the valve is closed automatically.
     *
     * If the valve is already closed or there is no credit left
     * before this call, the request is blocked entirely (no water
     * dispensed) and the method returns false.
     *
     * @param litres the number of litres that flowed through the meter
     * @return true if water was dispensed for this request,
     *         false if the request was blocked due to insufficient credit
     */
    public boolean recordConsumption(double litres) {
        if (litres <= 0) {
            throw new IllegalArgumentException("Litres must be positive.");
        }
        if (!valveOpen || creditBalance <= 0) {
            return false; // blocked - no credit / valve already shut
        }

        double cost = litres * COST_PER_LITRE;
        creditBalance -= cost;

        if (creditBalance <= 0) {
            creditBalance = 0;
            valveOpen = false; // out of credit - shut the valve
        }
        return true;
    }

    // --- read-only accessors (no public setters -> see part d) ---

    public String getMeterId() {
        return meterId;
    }

    public double getCreditBalance() {
        return creditBalance;
    }

    public boolean isValveOpen() {
        return valveOpen;
    }

    @Override
    public String toString() {
        return String.format("Meter[%s] balance=UGX %.2f valveOpen=%s",
                meterId, creditBalance, valveOpen);
    }
}