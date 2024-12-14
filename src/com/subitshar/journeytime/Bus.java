package com.subitshar.journeytime;
import java.util.Scanner;

public class Bus {

    private int busNo;
    private int Ac;
    private String fromToLocation;
    private int capacity;

    Bus() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Bus Number: ");
        busNo = sc.nextInt();
        System.out.println("Press 1 for Ac or 0 for Non-Ac: ");
        Ac = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter From-To-Location of Bus [e.g., Chennai to Delhi]: ");
        fromToLocation = sc.nextLine();
        System.out.println("Enter Total Capacity of Bus: ");
        capacity = sc.nextInt();
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int cap) {
        capacity = cap;
    }

    public int getAc() {
        return Ac;
    }

    public void setAc(int ac) {
        Ac = ac;
    }

    public int getBusNo() {
        return busNo;
    }

    public void setBusNo(int busnum) {
        busNo = busnum;
    }

    public String getLocation() {
        return fromToLocation;
    }

    public void setLocation(String location) {
        fromToLocation = location;
    }
}
