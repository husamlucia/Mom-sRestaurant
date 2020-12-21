package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleServer extends AbstractServer {

	public SimpleServer(int port) {
		super(port);
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		String msgString = msg.toString();
		if (msgString.startsWith("#warning")) {
			Warning warning = new Warning("Warning from server!");
			try {
				client.sendToClient(warning);
				System.out.format("Sent warning to client %s\n", client.getInetAddress().getHostAddress());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if(msgString.startsWith("#updateMeal ")){
			try {
				String[] attributes = msgString.substring(12).split("\\s+");
				int mealID = Integer.parseInt(attributes[0]);
				String name = attributes[1];
				Double price = Double.parseDouble(attributes[2]);
				String[] ing = Arrays.copyOfRange(attributes, 3, attributes.length);
				List<String> ingredients = Arrays.asList(ing);
				for(String str: ingredients){
					System.out.println(str);
				}

				MealService mService = new MealService();
				Meal meal = mService.findById(mealID);
				meal.setName(name);
				meal.setPrice(price);
				meal.setIngredients(ingredients);
				mService.update(meal);

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		else if(msgString.startsWith("#removeMeal ")){
			int id = Integer.parseInt(msgString.substring(12));
			MealService mService = new MealService();
			MenuService menuService = new MenuService();

			Meal meal = mService.findById(id);
			Menu newMenu = meal.getMenu();
			newMenu.getMeals().remove(meal);

			//menuService.update(newMenu);
			mService.delete(id);
		}
		else if(msgString.startsWith("#requestMenu ")){
			int id = Integer.parseInt(msgString.substring(13));
			//Should send to client list of Meals..

			BranchServices brDao = new BranchServices();

			Branch br = brDao.findById(id);
			Branch brGlobal = brDao.findById(1);


			List<Meal> meals = new ArrayList<Meal>(br.getMenu().getMeals());
			meals.addAll(brGlobal.getMenu().getMeals());


			Menu menu = new Menu();
			menu.setMeals(meals);
			try {
				client.sendToClient(menu);
				System.out.format("Sent menu to client %s\n", client.getInetAddress().getHostAddress());
			}
			 catch (IOException e){
			e.printStackTrace();
		}
		}
		else if(msgString.startsWith("#addBranch ")){
			// #addBranch 17:00 20:00
			try {
				String open = msgString.substring(11,16);
				String close = msgString.substring(17,22);

				BranchServices brDao = new BranchServices();
				MenuService menuDao = new MenuService();
				Branch newBranch = new Branch(open,close);
				brDao.save(newBranch);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		}
		else if(msgString.startsWith("#addMeal ")){

			try {
				String[] attributes = msgString.substring(9).split("\\s+");
				int branchID = Integer.parseInt(attributes[0]);
				String name = attributes[1];
				Double price = Double.parseDouble(attributes[2]);
				String[] ing = Arrays.copyOfRange(attributes, 3, attributes.length);
				List<String> ingredients = Arrays.asList(ing);
				for(String str: ingredients){
					System.out.println(str);
				}

				Meal newMeal = new Meal(name,price, ingredients);


				BranchServices brService = new BranchServices();
				MealService mealService = new MealService();

				Branch br = brService.findById(branchID);
				Menu menu = br.getMenu();

				newMeal.setMenu(menu);
				mealService.save(newMeal);

				menu.addMeal(newMeal);
				System.out.println("Done.");

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		}
		else if(msgString.startsWith("#getAllMeals")){
			//Should send to client list of Meals..
			System.out.println("1");
			MealService mealService = new MealService();
			System.out.println("1");

			List<Meal> meals = mealService.findAll();
			System.out.println("1");
			MenuPOJO menu = new MenuPOJO();
			System.out.println("1");
			menu.setMeals(meals);
			System.out.println("1");
			try {
				client.sendToClient(menu);
				System.out.format("Sent all meals to client %s\n", client.getInetAddress().getHostAddress());
			}
			catch (IOException e){
				e.printStackTrace();
			}
		}
	}

}
