package com.subitshar.journeytime;
import java.sql.*;

public class BusDbAccess {

    public void displayBusInfo() throws Exception {
        try (Connection con = Db_connection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM Bus")) {

            while (rs.next()) {
                System.out.println("Bus No: " + rs.getInt(1));
                System.out.println("Ac: " + (rs.getInt(2) == 1 ? "Yes" : "No"));
                System.out.println("Capacity: " + rs.getInt(4));
                System.out.println("Location: " + rs.getString(3));
                System.out.println("-----------------------------------------");
            }

            System.out.println("-----------------------------------------");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getCapacity(int busNo) throws Exception {
        String query = "SELECT capacity FROM Bus WHERE busNo = ?";
        int capacity = 0;

        try (Connection con = Db_connection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, busNo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    capacity = rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return capacity;
    }

    public void addBus(Bus bus) throws Exception {
        String query = "INSERT INTO Bus VALUES(?,?,?,?)";

        try (Connection con = Db_connection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, bus.getBusNo());
            ps.setInt(2, bus.getAc());
            ps.setString(3, bus.getLocation());
            ps.setInt(4, bus.getCapacity());
            

            ps.executeUpdate();
            System.out.println("Bus Added Successfully!...");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeBus(int busNo) throws Exception {
        String query = "DELETE FROM Bus WHERE busNo = ?";

        try (Connection con = Db_connection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, busNo);
            ps.executeUpdate();
            System.out.println("Bus Removed Successfully!...");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
