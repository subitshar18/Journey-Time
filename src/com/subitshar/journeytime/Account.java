package com.subitshar.journeytime;

import java.util.Scanner;

public class Account {
    private int userId;
    private String userName;
    private String password;
    private String accountType;

    public Account() {
        System.out.println("****** Welcome to Account Creation Page ******");
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter user name: ");
        userName = sc.nextLine();
        boolean flag = true;
        while (flag) {
            System.out.println("Please set Password:");
            String pass = sc.nextLine();
            System.out.println("Please Re-Enter Password:");
            String rPass = sc.nextLine();
            
            if (!pass.equals(rPass)) {
                System.out.println("Password Mis-matching. Please Try again...");
            } else {
                flag = false;
                password = rPass;
            }
        }
        
        System.out.println("Choose Account type:");
        System.out.println("Press 1 for user account or 0 for admin account");
        int type = sc.nextInt();
        accountType = (type == 0) ? "Admin" : "User";
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getAccountType() {
        return accountType;
    }

    public int getUserId() {
        return userId;
    }
}
