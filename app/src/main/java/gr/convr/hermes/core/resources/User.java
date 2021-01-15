package gr.convr.hermes.core.resources;


public class User {

    private String mail;
    private String name;
    private String password;
    private String id;
    private String customerId;

    private String companyVat;
    private boolean employee;
    private Long invoiceDueDate;


    public User(){}

    public User(String mail){

        this.mail = mail;

    }

    public User(String mail, String name, String password, String id){
        this.mail = mail;
        this.name = name;
        this.password = password;
        this.id = id;
        this.customerId = null;
        this.companyVat = null;
        this.employee = false;
        this.invoiceDueDate = 9999999999999999L;

    }




    public User(String mail, String name, String password, String id, String customerId){
        this(mail, name, password, id);
        this.customerId = customerId;
    }


    //Getters

    public String getMail() {
        return mail;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getPassword(){
        return password;
    }

    public String getCustomerId(){return customerId;}

    public String getCompanyVat(){return companyVat;}

    public boolean getEmployee(){return employee;}

    public Long getInvoiceDueDate(){return invoiceDueDate;}

}
