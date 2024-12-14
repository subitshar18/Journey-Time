package com.subitshar.journeytime;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            AccountDbAccess accountDbAccess = new AccountDbAccess();
            int choice = 0;
            while (choice != 4) {
                System.out.println("--------Welcome to Journey Time--------");
                System.out.println("1. Sign up");
                System.out.println("2. Login");
                System.out.println("3. Close Account");
                System.out.println("4. Exit");
                System.out.println("Please choose an option: ");
                choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1:
                        Account account = new Account();
                        accountDbAccess.addAccount(account);
                        accountDbAccess.displayAccountDetails(account);
                        System.out.println("Sign up successful! Now, please log in to access Your Account.");
                        break;

                    case 2:
                        Login login = new Login();
                        boolean isValid = accountDbAccess.accountValidation(login.getUserId(), login.getPassword());

                        if (isValid) {
                            String name = accountDbAccess.getUserName(login.getUserId());
                            System.out.println("****** Logging in Successfully! *******");
                            System.out.println("Welcome " + name);
                            String accountType = accountDbAccess.getAccountType(login.getUserId());

                            if ("Admin".equals(accountType))
                                login.Admin();
                            else
                                login.User();
                        } else {
                            System.out.println("Wrong credentials");
                        }
                        break;

                    case 3:
                        System.out.println("Enter your User Id: ");
                        int id = sc.nextInt();
                        sc.nextLine();
                        System.out.println("Enter your Account password: ");
                        String pass = sc.nextLine();
                        boolean validCredentials = accountDbAccess.accountValidation(id, pass);

                        if (validCredentials) {
                            accountDbAccess.closeAccount(id);
                            System.out.println("Account closed successfully!..");
                        }
                        else {
                            System.out.println("Password mismatching can't delete this Account");
                        }
                        
                        break;

                    case 4:
                        System.out.println("---------------Application closed!----------------");
                        break;

                    default:
                        System.out.println("Invalid choice. Please choose a valid option.");
                }
            }
        } catch (java.util.InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            sc.close();
        }
    }
}
