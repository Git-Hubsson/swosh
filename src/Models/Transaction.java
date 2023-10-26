package Models;

import java.time.LocalDateTime;

public class Transaction {
    private int transactionId;
    private int senderAccountId;
    private int receiverAccountId;
    private float amount;
    private LocalDateTime datetime;

    public Transaction(int senderAccountId, int receiverAccountId, float amount, LocalDateTime datetime) {
        this.senderAccountId = senderAccountId;
        this.receiverAccountId = receiverAccountId;
        this.amount = amount;
        this.datetime = datetime;
    }

    public Transaction(int transactionId, int senderAccountId, int receiverAccountId, float amount, LocalDateTime datetime) {
        this.transactionId = transactionId;
        this.senderAccountId = senderAccountId;
        this.receiverAccountId = receiverAccountId;
        this.amount = amount;
        this.datetime = datetime;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public void setSenderAccountId(int senderAccountId) {
        this.senderAccountId = senderAccountId;
    }

    public void setReceiverAccountId(int receiverAccountId) {
        this.receiverAccountId = receiverAccountId;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public int getSenderAccountId() {
        return senderAccountId;
    }

    public int getReceiverAccountId() {
        return receiverAccountId;
    }

    public float getAmount() {
        return amount;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }
}
