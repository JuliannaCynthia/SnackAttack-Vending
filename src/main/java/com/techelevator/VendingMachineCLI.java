package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
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
		int productsRemaining = 5;
		Map<String, Object> mapOfProducts = new HashMap<>();
		double cashOnHand = 0;

		try(Scanner fileScanner = new Scanner(vendingFile)) {
			while (fileScanner.hasNextLine()) {
				String[] productInfo = fileScanner.nextLine().split("\\,");
				String productID = String.valueOf(productInfo[0]);
				String name = productInfo[1];
				double price = Double.parseDouble(productInfo[2]);
				String type = productInfo[3];
				if (productInfo[3].equals("Candy")) {
					Candy candy = new Candy(productID, name, price);
					mapOfProducts.put(productID, candy);
				} else if (productInfo[3].equals("Drink")) {
					Drink drink = new Drink(productID, name, price);
					mapOfProducts.put(productID, drink);
				} else if (productInfo[3].equals("Munchy")) {
					Munchy munchy = new Munchy(productID, name, price);
					mapOfProducts.put(productID, munchy);
				} else if (productInfo[3].equals("Gum")) {
					Gum gum = new Gum(productID, name, price);
					mapOfProducts.put(productID, gum);
				}
			}
		}catch (FileNotFoundException e){
			System.out.println("Oops, something has gone wrong!");
		}

		while (toRun) {

			System.out.println("(1) " + MAIN_MENU_OPTION_DISPLAY_ITEMS + "\n(2) " + MAIN_MENU_OPTION_PURCHASE + "\n(3) Exit");
			String choice = userInput.nextLine();


				if (choice.equals("1")) {
					try(Scanner fileScanner = new Scanner(vendingFile)){
						while(fileScanner.hasNextLine()){
							String[] productInfo = fileScanner.nextLine().split("\\,");
							System.out.println(productInfo[0] + " is: " + productInfo[1] + ", Costs: " + productInfo[2] + " and is a " + productInfo[3] + "!");
							System.out.println("There are " + productsRemaining + " " + productInfo[0] + " left!" );
						}

					}catch(FileNotFoundException e){
						System.out.println("Oops, something has gone wrong!");
					}


				} else if (choice.equals("2")) {
					boolean stay = true;
					while(stay) {
						System.out.println("Current money provided: $" + cashOnHand);
						System.out.println("(1) Feed Money\n(2) Select Product\n(3) Finish Transaction");
						String userChoice = userInput.nextLine();
						if (userChoice.equals("1")) {
							System.out.println("Please enter the amount of money you have: ");
							cashOnHand += Double.parseDouble(userInput.nextLine());
							continue;
						} else if (userChoice.equals("2")) {
							System.out.println("Please enter a slot choice: ");
							String slotChoice = userInput.nextLine().toUpperCase();
							if (mapOfProducts.containsKey(slotChoice)) {

							}
						} else if (userChoice.equals("3")) {
							//giving change and exiting to the first menu
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
		}
	}

