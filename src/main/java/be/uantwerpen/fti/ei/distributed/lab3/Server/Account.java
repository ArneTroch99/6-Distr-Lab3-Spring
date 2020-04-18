package be.uantwerpen.fti.ei.distributed.lab3.Server;

public class Account {

    private String name;
    private int balance;
    private String address;
    private int children;
    private String partner;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public Account(String name, int balance, String address, int children, String partner) {
        this.name = name;
        this.balance = balance;
        this.address = address;
        this.children = children;
        this.partner = partner;
    }

    public Account() {
    }

    @Override
    public String toString() {
        String temp = "";
        temp = temp + name + ", " + balance + ", " + address + ", " + children + ", " + partner;
        return temp;
    }
}
