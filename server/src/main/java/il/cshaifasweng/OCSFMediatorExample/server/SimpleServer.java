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
		else if (msgString.startsWith("#login")){

			String[] attributes = msgString.substring(7).split("\\s+");
			String id = attributes[0];
			String password = attributes[1];
			Dao<Worker> dao = new Dao(Worker.class);
			List<Worker> workers = dao.findAll();
			System.out.println(id + ' ' + password);
			for(Worker worker: workers){
				String workerId = worker.getGovId();
				String workerPw = worker.getPassword();
				if(id.equals(workerId) && password.equals(workerPw)){
					try {
						client.sendToClient(worker);
						return;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			Warning warning = new Warning("Incorrect userId or password!");
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

				Dao<Meal> mService = new Dao(Meal.class);
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
			Dao<Meal> mService = new Dao(Meal.class);
			Dao<Menu> menuService = new Dao(Menu.class);

			Meal meal = mService.findById(id);
			Menu newMenu = meal.getMenu();
			newMenu.getMeals().remove(meal);

			//menuService.update(newMenu);
			mService.delete(id);
		}
		else if(msgString.startsWith("#requestMenu ")){
			int id = Integer.parseInt(msgString.substring(13));
			//Should send to client list of Meals..

			Dao<Branch> brDao = new Dao(Branch.class);

			Branch br = brDao.findById(id);

			List<Meal> meals = new ArrayList<Meal>(br.getMenu().getMeals());
			if (id > 1){
				Branch brGlobal = brDao.findById(1);
				meals.addAll(brGlobal.getMenu().getMeals());
			}


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

				Dao<Branch> brDao = new Dao(Branch.class);
				Dao<Menu> menuDao = new Dao(Menu.class);
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


				Dao<Branch> brService = new Dao(Branch.class);
				Dao<Meal> mealService = new Dao(Meal.class);

				Branch br = brService.findById(branchID);
				Menu menu = br.getMenu();

				newMeal.setMenu(menu);
				mealService.save(newMeal);

				menu.addMeal(newMeal);

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		}
		else if(msgString.startsWith("#getAllMeals")){
			//Should send to client list of Meals..
			Dao<Meal> mealService = new Dao(Meal.class);
			List<Meal> meals = mealService.findAll();
			MenuPOJO menu = new MenuPOJO();
			menu.setMeals(meals);

			try {
				client.sendToClient(menu);
				System.out.format("Sent all meals to client %s\n", client.getInetAddress().getHostAddress());
			}
			catch (IOException e){
				e.printStackTrace();
			}
		}

		else if(msgString.startsWith("#addDefaultWorkers")){
			//Should send to client list of Meals..
				initiateWorkers();
		}
		else if(msgString.startsWith("#getAllBranches")){
			try{
				Dao<Branch> branchService = new Dao(Branch.class);
				List<Branch> branches = branchService.findAll();
				BranchList ret = new BranchList(branches);
				client.sendToClient(ret);
			}
			catch (IOException e){
				e.printStackTrace();
			}
		}

	}

	void initiateWorkers(){

		try{
			Dao<Worker> workerDao = new Dao(Worker.class);
			Worker[] workers = new Worker[5];

			workers[0] = new Worker(5, "209146687", "Husam Lucia", "bestadmin123");
			workers[1] = new Worker(4, "209050202", "Samer Kharouba", "lovesamer");
			workers[2] = new Worker(3, "209146695", "Sahar Lucia", "sahar123");
			workers[3] = new Worker(2, "206214785", "Loai Marei", "loainoob1");
			workers[4] = new Worker(1, "209050203", "Dalia Khateb", "daliakhateb123");

			for (Worker worker: workers){
				workerDao.save(worker);
			}
			System.out.format("Done");
		}
		catch (Exception e){
			e.printStackTrace();
		}

	}



}
