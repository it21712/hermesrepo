package gr.convr.hermes.core.resources;


public class CustomerInfo {


    private boolean employee = false;
    private Long invoiceDueDate;

    public CustomerInfo(boolean employee, Long invoiceDueDate){
        this.employee = employee;
        this.invoiceDueDate = invoiceDueDate;
    }

    public CustomerInfo(boolean employee){
        this.employee = employee;
    }

    public CustomerInfo(){}

    public Long getInvoiceDueDate(){return invoiceDueDate;}
    public boolean isEmployee(){return employee;}

    public void setEmployee(boolean employee){ this.employee = employee; }
    public void setInvoiceDueDate(Long invoiceDueDate){ this.invoiceDueDate = invoiceDueDate; }




}
