public class ThreeWayRecognitionStrategy extends RecognitionStrategy {
    private int firstRecognitionOffset;
    private int secondRecognitionOffset;
    public ThreeWayRecognitionStrategy(int firstRecognitionOffset,
                                       int secondRecognitionOffset)
    {
        this.firstRecognitionOffset = firstRecognitionOffset;
        this.secondRecognitionOffset = secondRecognitionOffset;
    }
    public void calculateRevenueRecognitions(Contract contract) {
        Money[] allocation = contract.getRevenue().allocate(3);
        contract.addRevenueRecognition(new RevenueRecognition
                (contract, allocation[0], contract.getDateSigned()));
        contract.addRevenueRecognition(new RevenueRecognition
                (contract, allocation[1], contract.getDateSigned().addDays(firstRecognitionOffset)));
        contract.addRevenueRecognition(new RevenueRecognition
                (contract, allocation[2], contract.getDateSigned().addDays(secondRecognitionOffset)));
    }
}
