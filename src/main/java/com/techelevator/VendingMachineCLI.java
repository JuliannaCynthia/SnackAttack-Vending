package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/*
 * This class is provided to you as a *suggested* class to start
 * your project. Feel free to refactor this code as you see fit.
 */
public class VendingMachineCLI {

    private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
    private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";

    public static void main(String[] args) {
        VendingMachineCLI cli = new VendingMachineCLI();
        cli.run();

    }
    public void run() {
        Scanner userInput = new Scanner(System.in);
//		PrintWriter writer = new PrintWriter("Log.txt");
        boolean toRun = true;
        File vendingFile = new File("main.csv");
        int vendCount = 1;
        Map<String, Product> mapOfProducts = new HashMap<>();
        double cashOnHand = 0;
        double totalItemCost = 0.0;
        String type = "";
        LocalDateTime theDateTime = LocalDateTime.now();
        DateTimeFormatter targetFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");
//        theDateTime.format(targetFormat);

        try (Scanner fileScanner = new Scanner(vendingFile)) {
            while (fileScanner.hasNextLine()) {
                String[] productInfo = fileScanner.nextLine().split("\\,");
                String productID = String.valueOf(productInfo[0]);
                String name = productInfo[1];
                double price = Double.parseDouble(productInfo[2]);
                type = productInfo[3];
                if (type.equals("Candy")) {
                    Candy candy = new Candy(productID, name, price);
                    mapOfProducts.put(productID, candy);
                } else if (type.equals("Drink")) {
                    Drink drink = new Drink(productID, name, price);
                    mapOfProducts.put(productID, drink);
                } else if (type.equals("Munchy")) {
                    Munchy munchy = new Munchy(productID, name, price);
                    mapOfProducts.put(productID, munchy);
                } else if (type.equals("Gum")) {
                    Gum gum = new Gum(productID, name, price);
                    mapOfProducts.put(productID, gum);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Oops, something has gone wrong!");
        }
        File newFile = new File("Log.txt");
        try (PrintWriter writer = new PrintWriter(newFile)){
                while (toRun) {
                    System.out.println("(1) " + MAIN_MENU_OPTION_DISPLAY_ITEMS + "\n(2) " + MAIN_MENU_OPTION_PURCHASE + "\n(3) Exit");
                    String choice = userInput.nextLine();
                    if (choice.equals("1")) {
                        ///Prints the inventory from the file, through a map.
                        printVendingInventory(vendingFile, mapOfProducts);
                    } else if (choice.equals("2")) {
                        boolean stay = true;
                        while (stay) {
                            System.out.println("Current money provided: $" + String.format("%.2f", cashOnHand));
                            System.out.println("(1) Feed Money\n(2) Select Product\n(3) Finish Transaction");
                            String userChoice = userInput.nextLine();
                            if (userChoice.equals("1")) {
                                System.out.println("Please enter the amount of money you have: ");
                                String cashAdded = userInput.nextLine();
                                cashOnHand += Double.parseDouble(cashAdded);
                                String feedMoneyLine = theDateTime.format(targetFormat) + " FEED MONEY: $" + cashAdded + " CUSTOMER MONEY TOTAL: $" + String.format("%.2f",cashOnHand);
                                writer.println(feedMoneyLine);
                                continue;
                            } else if (userChoice.equals("2")) {
                                printVendingInventory(vendingFile, mapOfProducts);
                                System.out.println("Please enter a slot choice: ");
                                String slotChoice = userInput.nextLine().toUpperCase();
                                if (mapOfProducts.containsKey(slotChoice)) {
                                    System.out.println("You chose the " + mapOfProducts.get(slotChoice).getName() + "! It costs: " + mapOfProducts.get(slotChoice).getPrice() + "\n" + mapOfProducts.get(slotChoice).getMessage());

                                    if (mapOfProducts.get(slotChoice).getProductCount() > 0) {
                                        vendCount++;
                                        if (cashOnHand < mapOfProducts.get(slotChoice).getPrice()) {
                                            System.out.println("Uh Oh! You cant afford that!");
                                        } else {
                                            if (vendCount % 2 == 0) {
                                                cashOnHand += 1;
                                                System.out.println("Woo! It's August! BOGODO for you! Enjoy one dollar off your choice!");
                                            }
                                            cashOnHand -= mapOfProducts.get(slotChoice).getPrice();
                                            totalItemCost += mapOfProducts.get(slotChoice).getPrice();
                                            int currentCount = mapOfProducts.get(slotChoice).getProductCount();
                                            mapOfProducts.get(slotChoice).setProductCount(currentCount - 1);
                                            String productTransaction = theDateTime.format(targetFormat) + " " + mapOfProducts.get(slotChoice).getName() + " " + mapOfProducts.get(slotChoice).getSlotIdentifier() + " ITEM PRICE: $" + mapOfProducts.get(slotChoice).getPrice() + " CUSTOMER MONEY TOTAL: $" + String.format("%.2f",cashOnHand);
                                            writer.println(productTransaction);
                                        }
                                    } else {
                                        System.out.println("Sorry, we're sold out of that item! Pick again?");
                                    }
                                    continue;
                                }
                            } else if (userChoice.equals("3")) {
                                String changeAmount = theDateTime.format(targetFormat) + " GIVE CHANGE: $" + String.format("%.2f", cashOnHand) + " CUSTOMER MONEY TOTAL: $0.00";
                                writer.println(changeAmount);
                                if (cashOnHand != 0) {
                                    int quarters = 0;
                                    int dimes = 0;
                                    int nickels = 0;
                                    int pennies = 0;

                                    if (cashOnHand / 0.25 != 0) {
                                        quarters = (int) (cashOnHand / 0.25);
                                        cashOnHand -= quarters * 0.25;
                                    }
                                    if (cashOnHand / .1 != 0) {
                                        dimes = (int) (cashOnHand / 0.1);
                                        cashOnHand -= dimes * 0.1;
                                    }
                                    if (cashOnHand / 0.05 != 0) {
                                        nickels = (int) (cashOnHand / 0.05);
                                        cashOnHand -= nickels * 0.05;
                                    }
                                    if (cashOnHand / 0.01 != 0) {
                                        pennies = (int) (cashOnHand / 0.01);
                                        if (cashOnHand % 0.01 > 0) {
                                            pennies++;
                                        }
                                        cashOnHand = 0.0;
                                    }
                                    System.out.println("Your change is: " + quarters + " quarters, " + dimes + " dimes, " + nickels + " nickles, and " + pennies + " pennies!");
                                }
                                break;
                            }
                        }
                    } else if (choice.equals("3")) {
                        System.out.println("You have chosen to exit the Vending Machine. Have a nice day!");
                        toRun = false;

                    } else if (choice.equals("4")) {
                        ///Hidden menu optional
                    }
                }
        } catch (FileNotFoundException e) {
            System.out.println("An error has occurred.");
        }
    }
    public void printVendingInventory(File file, Map<String, Product> map) {
        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String[] productInfo = fileScanner.nextLine().split("\\,");

                System.out.println(productInfo[0] + " is: " + productInfo[1] + ", Costs: $" + productInfo[2] + " and is a " + productInfo[3] + "!");
                System.out.println("There are " + map.get(productInfo[0]).getProductCount() + " " + productInfo[1] + " left!");
                if (map.get(productInfo[0]).getProductCount() == 0) {
                    System.out.println("!SOLD OUT!\n");
                } else {
                    System.out.println();
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Oops, something has gone wrong!");
        }
    }
}