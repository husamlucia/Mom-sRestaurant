package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.Meal;
import il.cshaifasweng.OCSFMediatorExample.entities.Menu;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import il.cshaifasweng.OCSFMediatorExample.entities.Branch;
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
		else if(msgString.startsWith("#requestMenu ")){
			int id = Integer.parseInt(msgString.substring(13));
			//Should send to client list of Meals..

			BranchDao brDao = new BranchDao();
			brDao.openCurrentSession();

			Branch br = brDao.findById(id),
					brGlobal = brDao.findById(1);
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
			brDao.closeCurrentSession();
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
			String[] attributes = msgString.substring(9).split("\\s+");
			int branchID = Integer.parseInt(attributes[0]);
			String name = attributes[1];
			Double price = Double.parseDouble(attributes[2]);
			String[] ing = Arrays.copyOfRange(attributes, 3, attributes.length);
			List<String> ingredients = Arrays.asList(ing);


			BranchServices brService = new BranchServices();

			Branch br = brService.findById(branchID);

			MealService mealService = new MealService();

			Meal newMeal = new Meal(name,price, ingredients);
			newMeal.setMenu(br.getMenu());
			mealService.save(newMeal);

		}
	}

}
