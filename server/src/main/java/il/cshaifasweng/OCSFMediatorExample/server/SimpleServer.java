package il.cshaifasweng.OCSFMediatorExample.server;


import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.entities.Mapp;
import il.cshaifasweng.OCSFMediatorExample.entities.Menu;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class SimpleServer extends AbstractServer {

    public SimpleServer(int port) {
        super(port);
    }


    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        String msgString = msg.toString();

        if (msg.getClass().equals(Booking.class)) {
                Booking book = (Booking) msg;
                saveBooking(book, client);

        } else if (msgString.startsWith("#checkBooking ")) {
                String[] attributes = msgString.substring(14).split("\\s+");
                checkAvailableBooking(attributes, client);
        } else if (msgString.startsWith("#getAllBranches")) {
            sendAllBranches(client);
        } else if (msgString.startsWith("#order ")) {
            String[] attributes = msgString.substring(7).split("\\s+");
            createOrder(attributes, client);

        } else if (msg.getClass().equals(MealUpdate.class)) {
            MealUpdate mealUpdate = (MealUpdate) msg;
            dealWithMealUpdate(mealUpdate);
        } else if (msgString.startsWith("#requestUpdates ")) {
            int id = Integer.parseInt(msgString.substring(16));
            getBranchUpdates(id, client);
        } else if (msgString.startsWith("#login ")) {

            String[] attributes = msgString.substring(7).split("\\s+");
            String id = attributes[0];
            String password = attributes[1];
            confirmLogin(id, password, client);

        } else if (msgString.startsWith("#requestMenu ")) {
            int id = Integer.parseInt(msgString.substring(13));
            sendMenuToClient(id, client);
        } else if (msgString.startsWith("#addBranch ")) {
            // #addBranch 17:00 20:00
            String open = msgString.substring(11, 16);
            String close = msgString.substring(17, 22);
            addNewBranch(open, close);
        } else if (msgString.startsWith("#addDefaultWorkers")) {
            //Should send to client list of Meals..
            initiateWorkers();
        } else if (msgString.startsWith("#createmapswithtables")) {
            create_branches_with_maps_and_tables();
        }
        else if(msg.getClass().equals(Complaint.class)){
            Complaint complaint=(Complaint)msg;
            addNewComplaint(complaint);
        }
        else if(msgString.startsWith("#requestComplaints ")){
            int id = Integer.parseInt(msgString.substring(19));
            sendComplaintsToClient(id, client);
        }

    }

    void sendComplaintsToClient(int id, ConnectionToClient client){
        Dao<Branch> branchDao = new Dao(Branch.class);
        Branch branch = branchDao.findById(id);

        List<Complaint> complaints = branch.getComplaints();

        ComplaintEvent complaintEvent = new ComplaintEvent(complaints);
        try{
            client.sendToClient(complaintEvent);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void addNewComplaint(Complaint complaint){
        Dao<Complaint> complaintDao=new Dao<>(Complaint.class);
        Dao<Branch> branchDao=new Dao<>(Branch.class);
        Branch branch=complaint.getBranch();
        branch.addComplaint(complaint);
        try{
            branchDao.update(complaint.getBranch());
           // complaintDao.save(complaint);kjkjk
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    void checkAvailableBooking(String[] attributes, ConnectionToClient client){
        try {
            Dao<Branch> branchDao = new Dao(Branch.class);
            int brId = Integer.parseInt(attributes[0]);
            String date = attributes[1];
            String time = attributes[2];
            String area = attributes[3];
            int persons = Integer.parseInt(attributes[4]);

            Branch br = branchDao.findById(brId);
            Mapp map = br.getMap(area);

            List<Booking> bookings = br.book(date, time, area, persons);
            BookingEvent availableBookings = new BookingEvent(bookings);
            client.sendToClient(availableBookings);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void saveBooking(Booking book, ConnectionToClient client){
        try {
            Dao<Booking> bookDao = new Dao(Booking.class);
            System.out.println("Saving booking");
            try {
                bookDao.save(book);
            } catch (Exception e) {
                e.printStackTrace();
            }
            int bookId = book.getId();
            Warning warning = new Warning("Booking complete! Booking ID for cancelling booking: " + bookId);
            client.sendToClient(warning);
            System.out.format("Order %d added successfully\n", bookId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void createOrder(String[] attributes, ConnectionToClient client){
        String recipientName = "";
        String recipientPhone = "";
        String pickup = attributes[0];
        String different = attributes[1];
        String date = attributes[2];
        String customerName = attributes[3];
        String phoneNumber = attributes[4];
        String creditCard = attributes[5];
        double price = Double.parseDouble(attributes[6]);
        String address = "";
        int mealOffset = 7;
        if (pickup.equals("0")) {
            address = attributes[7];
            if (different.equals("0")) mealOffset = 8;
            else {
                recipientName = attributes[8];
                recipientPhone = attributes[9];
                mealOffset = 10;
            }
        }
        String[] mealIds = Arrays.copyOfRange(attributes, mealOffset, attributes.length);
        List<Meal> meals = new ArrayList<>();
        Dao<Meal> daoMeals = new Dao(Meal.class);
        for (String id : mealIds) {
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
        if (orderDao.findById(orderId) != null) {
            Warning warning = new Warning("Order complete! Order ID for cancelling order: " + orderId);
            try {
                client.sendToClient(warning);
                System.out.format("Order %d added successfully\n", orderId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void addNewBranch(String open, String close) {
        try {
            Dao<Branch> brDao = new Dao(Branch.class);
            Dao<Menu> menuDao = new Dao(Menu.class);
            Branch newBranch = new Branch(open, close);
            brDao.save(newBranch);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void sendAllBranches(ConnectionToClient client){
        try {
            Dao<Branch> branchService = new Dao(Branch.class);
            List<Branch> branches = branchService.findAll();
            BranchList ret = new BranchList(branches);
            client.sendToClient(ret);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void getBranchUpdates(int id, ConnectionToClient client) {
        try {
            Dao<Branch> branchDao = new Dao(Branch.class);
            Branch br = branchDao.findById(id);
            System.out.println(br.getMealUpdates().size());
            List<MealUpdate> updates = br.getMealUpdates();
            List<MealUpdate> toSend = new ArrayList<>();
            for (MealUpdate update : updates) {
                if (update.getStatus().equals("Awaiting")) toSend.add(update);
            }
            MealUpdateEvent updateEvent = new MealUpdateEvent(toSend);
            client.sendToClient(updateEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void dealWithMealUpdate(MealUpdate mealUpdate) {

        try {
            String status = mealUpdate.getStatus();
            int branchId;
            Dao<ImageInfo> imageDao = new Dao(ImageInfo.class);
            Dao<Branch> branchDao = new Dao(Branch.class);
            Dao<MealUpdate> mealUpdateDao = new Dao(MealUpdate.class);
            Dao<Menu> menuDao = new Dao(Menu.class);

            branchId = mealUpdate.getNewBranchId();
            Branch oldBranch = mealUpdate.getBranch();
            Branch newBranch = branchDao.findById(branchId);

            if (status.equals("Awaiting")) {
                if (mealUpdate.getOldMeal() != null && mealUpdate.getNewMeal() == null) {
                    System.out.println("Delete update");
                }
                imageDao.save(mealUpdate.getNewMeal().getImage());
                newBranch.getMealUpdates().add(mealUpdate);
                mealUpdateDao.save(mealUpdate);

            } else if (status.equals("Approved")) {//recheck
                try {
                    Meal oldMeal = mealUpdate.getOldMeal();
                    Meal newMeal = mealUpdate.getNewMeal();
                    Branch br = mealUpdate.getBr();
                    Menu menu = br.getMenu();
                    Dao<Meal> mealDao = new Dao(Meal.class);
                    if (oldMeal == null) {
                        //newMeal != null -> we need to add newMeal to branch menu.
                        newBranch.addMeal(newMeal);
                        branchDao.update(newBranch);
                        mealUpdateDao.update(mealUpdate);
                    } else if (newMeal == null) {
                        ;
                        mealUpdateDao.update(mealUpdate);
                        System.out.println("Deleting meal...");
                        newBranch.removeMeal(oldMeal);
                        branchDao.update(newBranch);
                        mealDao.update(oldMeal);
                    } else {
                        mealUpdateDao.update(mealUpdate);

                        oldMeal.setName(newMeal.getName());
                        oldMeal.setPrice(newMeal.getPrice());
                        oldMeal.setIngredients(newMeal.getIngredients());

                        int oldMealBranch = oldBranch.getId();
                        int newMealBranch = newBranch.getId();
                        if (oldMealBranch != newMealBranch) {
                            oldBranch.removeMeal(oldMeal);
                            newBranch.addMeal(oldMeal);
                        }
                        mealDao.update(oldMeal);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (status.equals("Denied")) {
                mealUpdateDao.update(mealUpdate);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    void sendMenuToClient(int id, ConnectionToClient client) {
        //Should send to client list of Meals..
        Dao<Branch> brDao = new Dao(Branch.class);
        Branch br = brDao.findById(id);
        List<Meal> meals = new ArrayList<Meal>(br.getMenu().getMeals());
        if (id > 1) {
            Branch brGlobal = brDao.findById(1);
            meals.addAll(brGlobal.getMenu().getMeals());
        }
        Menu menu = new Menu();
        menu.setMeals(meals);
        try {
            client.sendToClient(menu);
            System.out.format("Sent menu to client %s\n", client.getInetAddress().getHostAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void confirmLogin(String id, String password, ConnectionToClient client) {
        Dao<Worker> dao = new Dao(Worker.class);
        List<Worker> workers = dao.findAll();
        System.out.println(id + ' ' + password);
        for (Worker worker : workers) {
            String workerId = worker.getGovId();
            String workerPw = worker.getPassword();
            if (id.equals(workerId) && password.equals(workerPw)) {
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


    void initiateWorkers() {

        try {
            Dao<Worker> workerDao = new Dao(Worker.class);
            Worker[] workers = new Worker[5];

            workers[0] = new Worker(5, "209146687", "Husam Lucia", "bestadmin123");
            workers[1] = new Worker(4, "209050202", "Samer Kharouba", "lovesamer");
            workers[2] = new Worker(3, "209146695", "Sahar Lucia", "sahar123");
            workers[3] = new Worker(2, "206214785", "Loai Marei", "loainoob1");
            workers[4] = new Worker(1, "209050203", "Dalia Khateb", "daliakhateb123");

            for (Worker worker : workers) {
                workerDao.save(worker);
            }
            System.out.format("Done");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void create_branches_with_maps_and_tables() {
        try {
            Dao<Branch> brDao = new Dao(Branch.class);
            Dao<Mapp> mapDao = new Dao(Mapp.class);
            Dao<Tablee> tDao = new Dao(Tablee.class);

            Branch br1 = new Branch("09:00", "00:00");
            Branch br2 = new Branch("09:00", "00:00");
            Mapp insideMap1 = new Mapp(br1, "inside");
            Mapp outsideMap1 = new Mapp(br1, "outside");


            Tablee table1 = new Tablee(2, insideMap1);
            Tablee table2 = new Tablee(3, insideMap1);
            Tablee table3 = new Tablee(4, insideMap1);
            Tablee table4 = new Tablee(4, insideMap1);
            Tablee table5 = new Tablee(4, insideMap1);
            Tablee table6 = new Tablee(3, insideMap1);
            insideMap1.addTable(table1);
            insideMap1.addTable(table2);
            insideMap1.addTable(table3);
            insideMap1.addTable(table4);
            insideMap1.addTable(table5);
            insideMap1.addTable(table6);


            Tablee table7 = new Tablee(2, outsideMap1);
            Tablee table8 = new Tablee(2, outsideMap1);
            Tablee table9 = new Tablee(3, outsideMap1);
            Tablee table10 = new Tablee(3, outsideMap1);
            Tablee table11 = new Tablee(4, outsideMap1);
            Tablee table12 = new Tablee(4, outsideMap1);
            outsideMap1.addTable(table7);
            outsideMap1.addTable(table8);
            outsideMap1.addTable(table9);
            outsideMap1.addTable(table10);
            outsideMap1.addTable(table11);
            outsideMap1.addTable(table12);


            Mapp insideMap2 = new Mapp(br2, "inside");
            Mapp outsideMap2 = new Mapp(br2, "outside");

            Tablee tablee1 = new Tablee(2, insideMap2);
            Tablee tablee2 = new Tablee(3, insideMap2);
            Tablee tablee3 = new Tablee(4, insideMap2);
            Tablee tablee4 = new Tablee(4, insideMap2);
            Tablee tablee5 = new Tablee(4, insideMap2);
            Tablee tablee6 = new Tablee(3, insideMap2);
            insideMap2.addTable(tablee1);
            insideMap2.addTable(tablee2);
            insideMap2.addTable(tablee3);
            insideMap2.addTable(tablee4);
            insideMap2.addTable(tablee5);
            insideMap2.addTable(tablee6);


            Tablee tablee7 = new Tablee(2, outsideMap2);
            Tablee tablee8 = new Tablee(2, outsideMap2);
            Tablee tablee9 = new Tablee(3, outsideMap2);
            Tablee tablee10 = new Tablee(3, outsideMap2);
            Tablee tablee11 = new Tablee(4, outsideMap2);
            Tablee tablee12 = new Tablee(4, outsideMap2);
            outsideMap2.addTable(tablee7);
            outsideMap2.addTable(tablee8);
            outsideMap2.addTable(tablee9);
            outsideMap2.addTable(tablee10);
            outsideMap2.addTable(tablee11);
            outsideMap2.addTable(tablee12);


            brDao.save(br1);
            brDao.save(br2);
            System.out.println("Done");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
