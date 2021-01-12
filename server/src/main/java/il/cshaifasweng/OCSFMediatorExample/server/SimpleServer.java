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
		else if (msgString.startsWith("#login ")){

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
		else if(msgString.startsWith("#order ")){
			String recipientName = "";
			String recipientPhone = "";
			String[] attributes = msgString.substring(7).split("\\s+");
			System.out.print(msgString);
			String pickup = attributes[0];
			String different = attributes[1];
			String date = attributes[2];
			String customerName = attributes[3];
			String phoneNumber = attributes[4];
			String creditCard = attributes[5];
			double price = Double.parseDouble(attributes[6]);
			String address = "";
			int mealOffset = 7;
			if(pickup.equals("0")){
				address = attributes[7];
				if(different.equals("0")) mealOffset = 8;
				else {
					 recipientName = attributes[8];
					 recipientPhone = attributes[9];
					mealOffset = 10;
				}
			}
			String[] mealIds = Arrays.copyOfRange(attributes, mealOffset, attributes.length);
			List<Meal> meals = new ArrayList<>();
			Dao<Meal> daoMeals = new Dao(Meal.class);
			for(String id: mealIds){
				Meal meal = daoMeals.findById(Integer.parseInt(id));
				meals.add(meal);
			}


			CustomerDetails details = new CustomerDetails(customerName, phoneNumber, creditCard);
			Dao<CustomerDetails> detailsDao = new Dao(CustomerDetails.class);
			detailsDao.save(details);
			Dao<Order> orderDao = new Dao(Order.class);
			Order order = new Order(meals, pickup, different, details, recipientName, recipientPhone, address, price);
			orderDao.save(order);
			int orderId = order.getId();
			if (orderDao.findById(orderId) != null){
				Warning warning = new Warning("Order complete! Order ID for cancelling order: " + orderId);
				try {
					client.sendToClient(warning);
					System.out.format("Order %d added successfully\n", orderId);
				}
				catch (IOException e){
					e.printStackTrace();
				}
			}

			//String message = "#order pickup/delivery different date customername phonenumber creditcard optional:recipientname optional:recipientphone optional:address meals:
			//substring=7
			//if pickup=1 -> offset = 6
			//if pickup=0 ->
			//  if different=0 -> offset = 7
			//  if different -> offset = 9
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
