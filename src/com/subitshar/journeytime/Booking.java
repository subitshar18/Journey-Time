package com.subitshar.journeytime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Booking {
    private int Booking_No;
    private String passengerName;
    private int NumOfPassengers;
    private int busNo;
    private Date date;
    private int userId;

    public Booking() throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Bus no: ");
        busNo = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter name of passenger: ");
        passengerName = sc.nextLine();
        System.out.println("Enter Num of Passengers including yourself: ");
        NumOfPassengers = sc.nextInt();
        System.out.println("Enter date dd-mm-yyyy");
        String dateInput = sc.next();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            date = dateFormat.parse(dateInput);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Booking(int busNo, Date date, int numOfPassengers) {
        this.busNo = busNo;
        NumOfPassengers = numOfPassengers;
        this.date = date;
    }

    public void setBookingNo(int Booking_No) {
        this.Booking_No = Booking_No;
    }

    public int getBookingNo() {
        return Booking_No++;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public int getNumOfPassengers() {
        return NumOfPassengers;
    }

    public int getBusNo() {
        return busNo;
    }

    public Date getDate() {
        return date;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String stringFormatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(date);
    }

    public boolean isAvailable() throws Exception {
        BusDbAccess busDbAccess = new BusDbAccess();
        int capacity = busDbAccess.getCapacity(busNo);
        BookingDbAccess bookingDbAccess = new BookingDbAccess();
        int booked = bookingDbAccess.getBookedCount(busNo, date);
        int seats = capacity - booked;

        if (booked + NumOfPassengers > capacity)
            System.out.println("Available seats is: " + seats);

        return booked + NumOfPassengers < capacity;
    }
}
