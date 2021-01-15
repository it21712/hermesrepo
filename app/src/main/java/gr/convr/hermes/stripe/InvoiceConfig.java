package gr.convr.hermes.stripe;

public class InvoiceConfig {

    public static class InvoiceItemParams{

        private String productId;
        private String currency;
        private String customerId;
        private String companyName;

        public InvoiceItemParams(String productId, String currency, String customerId) {
            this.productId = productId;
            this.currency = currency;
            this.customerId = customerId;
        }

        public InvoiceItemParams(String productId, String currency, String customerId, String companyName){
            this(productId, currency, customerId);
            this.companyName = companyName;
        }

        public InvoiceItemParams(String companyName){this.companyName=companyName;}

        public InvoiceItemParams(String productId, String currency, String customerId, Long date) {
            this(productId, currency, customerId);
        }

        public String getProductId() {
            return productId;
        }

        public String getCurrency() {
            return currency;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }
    }

    public static class InvoiceParams{

        private String customerId;
        private String collection_method;
        private int days_until_due;

        public InvoiceParams(String customerId, String collection_method, int days_until_due) {
            this.customerId = customerId;
            this.collection_method = collection_method;
            this.days_until_due = days_until_due;
        }

        public String getCustomerId() {
            return customerId;
        }

        public String getCollection_method() {
            return collection_method;
        }

        public int getDays_until_due() {
            return days_until_due;
        }

    }
}
