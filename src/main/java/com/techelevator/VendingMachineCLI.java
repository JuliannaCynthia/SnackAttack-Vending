package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/*
 * This class is provided to you as a *suggested* class to start
 * your project. Feel free to refactor this code as you see fit.
 */
public class VendingMachineCLI {

    private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
    private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
    File vendingFile = new File("main.csv");
    File file = new File("log.txt");

    Logger logger = new Logger(file);
    VendingGraphics vendingGraphics = new VendingGraphics();
    String dateAndTime = "" + logger.dateAndTime();


    public static void main(String[] args) {
        VendingMachineCLI cli = new VendingMachineCLI();
        cli.run();

    }
    public void run() {

        Scanner userInput = new Scanner(System.in);
//		PrintWriter writer = new PrintWriter("Log.txt");
        boolean toRun = true;

        int vendCount = 1;

        Map<String, Product> mapOfProducts = new HashMap<>();

        Map<String, int[]> salesReport= new HashMap<>();
        Set<String> saleKeys = salesReport.keySet();

        double cashOnHand = 0;

        double totalItemCost = 0.0;

        String type = "";
        vendingGraphics.platformBanner();

        mapCreator(vendingFile, mapOfProducts, type);
        salesReportMapCreator(vendingFile,salesReport);

                while (toRun) {
                    vendingGraphics.mainMenu();
                    String choice = userInput.nextLine();
                    if (choice.equals("1")) {
                        ///Prints the inventory from the file, through a map.
                        printVendingInventory(vendingFile, mapOfProducts);
                    } else if (choice.equals("2")) {
                        boolean stay = true;
                        while (stay) {
                            System.out.println("Current money provided: $" + String.format("%.2f", cashOnHand));
                            vendingGraphics.purchaseMenu();
                            String userChoice = userInput.nextLine();
                            if (userChoice.equals("1")) {
                                vendingGraphics.moneyBanner();
                                System.out.println("Please enter the amount of money in whole dollars (any amount in change will not be accepted): ");
                                String cashAdded = userInput.nextLine();
                                double preRoundedAmount = Double.parseDouble(cashAdded);
                                double roundedAmount = Math.round(preRoundedAmount);
                                if(roundedAmount > preRoundedAmount){
                                    roundedAmount--;
                                }
                                cashOnHand += roundedAmount;
                                String message = "FEED MONEY: $" + cashAdded + " CUSTOMER MONEY TOTAL: $" + String.format("%.2f",cashOnHand);
                                logger.write(message);
                                continue;
                            } else if (userChoice.equals("2")) {
                                printVendingInventory(vendingFile, mapOfProducts);
                                System.out.println("Please enter a slot choice: ");
                                String slotChoice = userInput.nextLine().toUpperCase();
                                if (mapOfProducts.containsKey(slotChoice)) {
                                    System.out.println("You chose the " + mapOfProducts.get(slotChoice).getName() + "! It costs: " + mapOfProducts.get(slotChoice).getPrice() + "\n" + mapOfProducts.get(slotChoice).getMessage());
                                    if (mapOfProducts.get(slotChoice).getProductCount() > 0) {
                                        vendCount++;
                                        if (vendCount % 2 == 1) {
                                            cashOnHand += 1;
                                            (salesReport.get(mapOfProducts.get(slotChoice).getName()))[1] = (salesReport.get(mapOfProducts.get(slotChoice).getName()))[1] +1;

                                            System.out.println("Woo! It's August! BOGODO for you! Enjoy one dollar off your choice!");
                                            vendingGraphics.bogodo();
                                        }
                                        if (cashOnHand < mapOfProducts.get(slotChoice).getPrice()) {
                                            System.out.println("Uh Oh! You cant afford that!");
                                            vendingGraphics.angryNoAfford();
                                        } else {
                                            cashOnHand -= mapOfProducts.get(slotChoice).getPrice();
                                            totalItemCost += mapOfProducts.get(slotChoice).getPrice();
                                            int currentCount = mapOfProducts.get(slotChoice).getProductCount();
                                            mapOfProducts.get(slotChoice).setProductCount(currentCount - 1);

                                            (salesReport.get(mapOfProducts.get(slotChoice).getName()))[0] = (salesReport.get(mapOfProducts.get(slotChoice).getName()))[0] +1;

                                            String productTransaction =  mapOfProducts.get(slotChoice).getName() + " " + mapOfProducts.get(slotChoice).getSlotIdentifier() + " ITEM PRICE: $" + mapOfProducts.get(slotChoice).getPrice() + " CUSTOMER MONEY TOTAL: $" + String.format("%.2f",cashOnHand);
                                            logger.write(productTransaction);
                                        }
                                    } else {
                                        System.out.println("Sorry, we're sold out of that item! Pick again?");
                                        vendingGraphics.soldOut();
                                    }
                                    continue;
                                }
                            } else if (userChoice.equals("3")) {
                                String changeAmount = "GIVE CHANGE: $" + String.format("%.2f", cashOnHand) + " CUSTOMER MONEY TOTAL: $0.00";
                                logger.write(changeAmount);
                                String message = changeCounter(cashOnHand);
                                System.out.println(message);
                                break;
                            }
                        }
                    } else if (choice.equals("3")) {
                        System.out.println("You have chosen to exit the Vending Machine. Have a nice day!");
                        toRun = false;

                        vendingGraphics.platformBanner();
                    } else if (choice.equals("4")) {

                        String fileSuffix = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
                        File saleFile = new File(fileSuffix + "salesreport.txt");

                        try {
                            saleFile.createNewFile();
                        } catch (IOException e) {
                            System.out.println("Problem creating new file");;
                        }

                        Logger saleLogger = new Logger(saleFile);
                        for(String key: saleKeys){
                            String message = salesReport.get(key)[0]+ "|" + salesReport.get(key)[1];
                            saleLogger.addCreateSalesReport(key, message);
                        }
                        saleLogger.addCreateSalesReport("End Of Current Session Sales ", " $" + totalItemCost + "\n");

                    }
                }
    }

//    public void printSaleReport(File file, Map<String, int[]> map){
//        try(Scanner fileScanner = new Scanner(file)){
//
//        }catch(FileNotFoundException e){
//            System.out.println("File is not available.");
//        }
//    }

    public void printVendingInventory(File file, Map<String, Product> map) {
        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String[] productInfo = fileScanner.nextLine().split("\\,");

                System.out.println(productInfo[0] + " is: " + productInfo[1] + ", Costs: $" + productInfo[2] + " and is a " + productInfo[3] + "!");
                System.out.println("There are " + map.get(productInfo[0]).getProductCount() + " " + productInfo[1] + " left!");
                if (map.get(productInfo[0]).getProductCount() == 0) {
                    System.out.println("!SOLD OUT!\n");
                    vendingGraphics.soldOut();
                } else {
                    System.out.println();
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Oops, something has gone wrong!");
        }
    }


    public void mapCreator (File file, Map <String,Product> mapOfProducts, String type){

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
    }

    public void salesReportMapCreator(File file, Map<String, int[]> map){
        try(Scanner fileScanner = new Scanner(vendingFile)){
            while(fileScanner.hasNextLine()){
                String[] productInfo = fileScanner.nextLine().split("\\,");
                String name = productInfo[1];
                int[] salesReport = {0,0};
                map.put(name, salesReport);
            }

        }catch(FileNotFoundException e){
            System.out.println("Error. File not usable.");
        }
    }

    public String changeCounter (double cashOnHand){
        String message = "";
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
            } message = "Your change is: " + quarters + " quarters, " + dimes + " dimes, " + nickels + " nickels, and " + pennies + " pennies!";
        } return message;
    }
}