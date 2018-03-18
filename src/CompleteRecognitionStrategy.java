public class CompleteRecognitionStrategy extends RecognitionStrategy {
    public void calculateRevenueRecognitions(Contract contract) {
        contract.addRevenueRecognition(new RevenueRecognition(contract, contract.getRevenue(), contract.getDateSigned()));
    }
}
