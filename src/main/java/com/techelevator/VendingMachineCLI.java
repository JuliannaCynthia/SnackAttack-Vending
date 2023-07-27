package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

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



				} else if (choice.equals("3")) {
					System.out.println("You have chosen to exit the Vending Machine. Have a nice day!");
					toRun = false;

				} else if (choice.equals("4")) {
					///Hidden menu optional
				}

			}
		}
	}

