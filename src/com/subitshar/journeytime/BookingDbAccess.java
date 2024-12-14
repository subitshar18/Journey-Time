package com.subitshar.journeytime;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class BookingDbAccess {

    public int getBookedCount(int busNo, Date date) throws Exception {
        String query = "SELECT SUM(NumOfPassengers) FROM Booking WHERE bus_no = ? AND travel_date = ?";
        Connection con = Db_connection.getConnection();
        try (PreparedStatement ps = con.prepareStatement(query)) {
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            ps.setInt(1, busNo);
            ps.setDate(2, sqlDate);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        } finally {
            con.close();
        }
    }

    public void addBooking(Booking booking) throws Exception {
        String query = "INSERT INTO Booking (passenger_Name, NumOfPassengers, bus_no, travel_date, user_id) VALUES (?, ?, ?, ?, ?)";
        Connection con = Db_connection.getConnection();
        try (PreparedStatement ps = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, booking.getPassengerName());
            ps.setInt(2, booking.getNumOfPassengers());
            ps.setInt(3, booking.getBusNo());
            java.sql.Date sqlDate = new java.sql.Date(booking.getDate().getTime());
            ps.setDate(4, sqlDate);
            ps.setInt(5, booking.getUserId());
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();

            if (generatedKeys.next()) {
                int lastInsertedBookingNo = generatedKeys.getInt(1);
                booking.setBookingNo(lastInsertedBookingNo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            con.close();
        }
    }

    public static void displayBookingDetails(Booking booking) throws Exception {
        Connection con = Db_connection.getConnection();
        String query = "SELECT b.Booking_No, b.passenger_Name, b.NumOfPassengers, b.bus_no, b.travel_date, b.user_id, s.ac, s.fromToLocation FROM Booking b JOIN Bus s ON b.bus_no = s.busNo WHERE b.Booking_No = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setInt(1, booking.getBookingNo());

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    java.sql.Date travelDate = rs.getDate(5);

                    System.out.println("----------------Booking Details!---------------");
                    System.out.println("Hi! " + rs.getString(2));
                    System.out.println("Booking Number: " + rs.getInt(1));
                    System.out.println("For your User Id: " + rs.getInt(6));
                    System.out.println("Number of Passengers: " + rs.getInt(3));
                    System.out.println("Bus No: " + rs.getInt(4));
                    System.out.println("Ac: " + rs.getInt(7));
                    System.out.println("Location: " + rs.getString(8));
                    System.out.println("Travel Date: " + travelDate);
                    System.out.println("----------------Happy Journey!-----------------");
                } else {
                    System.out.println("No booking found with Booking_No: " + booking.getBookingNo());
                }
            }
        } finally {
            con.close();
        }
    }

    public int availableSeats(int busNo, Date date) throws Exception {
        Connection con = Db_connection.getConnection();
        String query = "SELECT (SELECT capacity FROM Bus WHERE busNo = ?) - COALESCE((SELECT SUM(NumOfPassengers) FROM Booking WHERE bus_no = ? AND travel_date = ?), 0)";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, busNo);
            ps.setInt(2, busNo);
            ps.setDate(3, new java.sql.Date(date.getTime()));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    return 0;
                }
            }
        } finally {
            con.close();
        }
    }

    public void cancelBooking(int bookingNo) throws Exception {
        Connection con = Db_connection.getConnection();
        String query = "DELETE from Booking where Booking_No = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setInt(1, bookingNo);
            preparedStatement.executeUpdate();
        } finally {
            con.close();
        }
    }

    public void cancelBookingByUserId(int userId) throws Exception {
        Connection con = Db_connection.getConnection();
        String cnt_query = "SELECT count(*) FROM Booking WHERE user_id = ?";
        String query = "DELETE FROM Booking WHERE user_id = ?";

        try (PreparedStatement countStatement = con.prepareStatement(cnt_query)) {
            countStatement.setInt(1, userId);
            ResultSet rs = countStatement.executeQuery();

            rs.next();
            int cnt = rs.getInt(1);

            if (cnt > 0) {
                try (PreparedStatement deleteStatement = con.prepareStatement(query)) {
                    deleteStatement.setInt(1, userId);
                    deleteStatement.executeUpdate();
                }
            } else {
                return; // No booking found for the given user, nothing to delete
            }
        } finally {
            con.close();
        }
    }

    public boolean showingHistory(int userId) throws Exception {
        Connection con = Db_connection.getConnection();
        String query = "SELECT b.Booking_No, b.passenger_Name, b.NumOfPassengers, b.bus_no, b.travel_date, s.ac, s.fromToLocation FROM Booking b JOIN Bus s ON b.bus_no = s.busNo WHERE b.user_id = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                int flag = 0;

                while (rs.next()) {
                    if (flag == 0) {
                        System.out.println("----------------History of Booking Details!---------------");
                        System.out.println("Hi! " + rs.getString("passenger_Name"));
                    }
                    flag++;
                    System.out.println(flag + ".");
                    System.out.println("Your Booking Number: " + rs.getInt(1));
                    System.out.println("Number of Passengers: " + rs.getInt(3));
                    System.out.println("Bus No: " + rs.getInt(4));
                    System.out.println("Ac: " + rs.getInt(6));
                    System.out.println("Location: " + rs.getString(7));
                    java.sql.Date travelDate = rs.getDate(5);
                    System.out.println("Travel Date: " + travelDate);
                    System.out.println("------------------------------------------------------");
                }
                if (flag == 0) {
                    return false;
                }
            }
        } finally {
            con.close();
        }
        
        return true;
    }
}
