package com.subitshar.journeytime;
import java.util.Scanner;

public class Login {
    private int userId;
    private String password;
    private Scanner sc = new Scanner(System.in);

    public Login() {
        System.out.println("****** Welcome to Login Page ******");
        System.out.println("Enter your user Id: ");
        userId = sc.nextInt();
        sc.nextLine(); // Consume the newline character
        System.out.println("Enter your Password: ");
        password = sc.nextLine();
    }

    public int getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public void Admin() throws Exception {
        BusDbAccess busDbAccess = new BusDbAccess();
        int choice = 0;
        while (choice != 3) {
            System.out.println("1. Add Bus");
            System.out.println("2. Remove Bus");
            System.out.println("3. Logout");
            System.out.println("Choose any one above options");

            try {
                choice = sc.nextInt();
                sc.nextLine(); // Consume the newline character
                switch (choice) {
                    case 1:
                        Bus bus = new Bus();
                        busDbAccess.addBus(bus);
                        break;
                    case 2:
                        System.out.println("Enter the Bus Number you want to remove");
                        int busno = sc.nextInt();
                        busDbAccess.removeBus(busno);
                        break;
                    case 3:
                        System.out.println("****** Logged out Successfully! *******");
                        break;
                    default:
                        System.out.println("Invalid choice. Please choose again.");
                        break;
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine(); // Consume the invalid input
            }
        }
    }

    public void User() throws Exception {
        int choice = 0;
        while (choice != 4) {
            System.out.println("1. Booking");
            System.out.println("2. Cancellation");
            System.out.println("3. Show Booking History");
            System.out.println("4. Logout");
            System.out.println("Choose any one above options");

            try {
                choice = sc.nextInt();
                sc.nextLine(); // Consume the newline character
                switch (choice) {
                    case 1:
                        makeBooking();
                        break;
                    case 2:
                        cancelBooking(userId);
                        break;
                    case 3:
                        showHistory();
                        break;
                    case 4:
                        System.out.println("****** Logged out Successfully! *******");
                        break;
                    default:
                        System.out.println("Invalid choice. Please choose again.");
                        break;
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine(); // Consume the invalid input
            }
        }
    }

    private void makeBooking() throws Exception {
        BusDbAccess busDbAccess = new BusDbAccess();
        BookingDbAccess bookingDbAccess = new BookingDbAccess();
        System.out.println("*******Showing List of Buses******");
        busDbAccess.displayBusInfo();
        Booking booking = new Booking();
        booking.setUserId(userId);
        System.out.println("Available Seats for BusNo-" + booking.getBusNo() +
                " On this date-" + booking.stringFormatDate(booking.getDate()) +
                " is: " + bookingDbAccess.availableSeats(booking.getBusNo(), booking.getDate()));

        System.out.print("Do you want to confirm your booking? (yes/no): ");

        String confirmation = sc.nextLine().trim().toLowerCase();

        if (confirmation.equals("yes")) {
            if (booking.isAvailable()) {
                bookingDbAccess.addBooking(booking);
                System.out.println("Your Booking is Confirmed");
                bookingDbAccess.displayBookingDetails(booking);
            } else {
                System.out.println("Sorry. Bus is full. Try another bus or date.");
            }
        } else {
            System.out.println("Your Booking is not Confirmed");
        }
    }

    private void cancelBooking(int userId) throws Exception {
        BookingDbAccess bookingDbAccess = new BookingDbAccess();
        boolean res = bookingDbAccess.showingHistory(userId);
        if (res) {
            System.out.println("Enter Booking Number to cancel:");
            int bookingId = sc.nextInt();
            bookingDbAccess.cancelBooking(bookingId);
            System.out.println("---Booking Successfully Cancelled!---");
        } else {
            System.out.println("Nothing to cancel because you have not started booking yet.");
        }
    }

    private void showHistory() throws Exception {
        BookingDbAccess bookingDbAccess = new BookingDbAccess();
        boolean res = bookingDbAccess.showingHistory(userId);
        if (!res)
            System.out.println("---No History of booking found with userId: " + userId + "---");
    }
}
