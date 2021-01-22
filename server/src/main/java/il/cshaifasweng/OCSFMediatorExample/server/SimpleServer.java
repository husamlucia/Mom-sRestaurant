package il.cshaifasweng.OCSFMediatorExample.server;


import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.entities.Mapp;
import il.cshaifasweng.OCSFMediatorExample.entities.Menu;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import javassist.compiler.ast.Pair;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
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
        } else if (msgString.startsWith("#cancelOrder ")) {
            int id = Integer.parseInt(msgString.substring(13));
            cancelOrder(id, client);
        } else if (msg.getClass().equals(ReportRequest.class)) {
            requestReports((ReportRequest) msg, client);
        } else if (msgString.startsWith("#cancelBooking ")) {
            System.out.println("test1");
            int id = Integer.parseInt(msgString.substring(15));
            cancelBooking(id, client);
        } else if (msgString.startsWith("#checkBooking ")) {
            String[] attributes = msgString.substring(14).split("\\s+");
            checkAvailableBooking(attributes, client);
        } else if (msgString.startsWith("#getAllBranches")) {
            sendAllBranches(client);
        } else if (msg.getClass().equals(Order.class)) {
            saveOrder((Order) msg, client);
        }
//        else if (msgString.startsWith("#order ")) {
//            String[] attributes = msgString.substring(7).split("\\s+");
//            createOrder(attributes, client);
//        }
        else if (msg.getClass().equals(MealUpdate.class)) {
            MealUpdate mealUpdate = (MealUpdate) msg;
            dealWithMealUpdate(mealUpdate);
            getBranchUpdates(mealUpdate.getBranch().getId(), client);
        } else if (msgString.startsWith("#requestUpdates ")) {
            int id = Integer.parseInt(msgString.substring(16));
            getBranchUpdates(id, client);
        } else if (msgString.startsWith("#requestMap ")) {
            String[] attributes = msgString.substring(12).split("\\s+");

            int id = Integer.parseInt(attributes[0]);
            String date = attributes[1];
            String hour = attributes[2];
            String area = attributes[3];
            requestMap(id, date, hour, area, client);
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
        } else if (msg.getClass().equals(Complaint.class)) {
            Complaint complaint = (Complaint) msg;
            addNewComplaint(complaint);
        } else if (msgString.startsWith("#requestComplaints ")) {
            int id = Integer.parseInt(msgString.substring(19));
            sendComplaintsToClient(id, client);
        } else if (msgString.startsWith("#requestPurpleLetters")) {
            sendPurpleLetters(client);
        } else if (msg.getClass().equals(PurpleLetter.class)) {
            updatePurpleLetter((PurpleLetter) msg);
            sendPurpleLetters(client);
        }

    }


    void requestReports(ReportRequest request,ConnectionToClient client) {
        try{
            Dao<Branch> brDao = new Dao(Branch.class);
            int id = request.getId();
            int month = request.getMonth();
            Branch br = brDao.findById(id);

            List<ChartInput> chartInputs = new ArrayList<>();

            if(request.isOrders()){
                chartInputs.add(getTotalOrders(br, month));
            }
            if(request.isCancelledOrders()){
                chartInputs.add(getCancelledOrders(br, month));
            }
            if(request.isComplaints()){
                chartInputs.add(getComplaints(br, month));
            }

            ChartEvent event = new ChartEvent(chartInputs);

            try {
                client.sendToClient(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }


    ChartInput getTotalOrders(Branch br, int month){

        LocalDate now = LocalDate.now();
        YearMonth yearMonthObject = YearMonth.of(now.getYear(), month);
        int daysInMonth = yearMonthObject.lengthOfMonth();

        int[] toReturn = new int[daysInMonth];

        List<Order> orders = br.getOrders();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate orderDate;
        System.out.println(orders.size());
        for (Order order : orders) {
            orderDate = LocalDate.parse(order.getDate(), formatter);
            if (orderDate.getMonthValue() == month) {
                toReturn[orderDate.getDayOfMonth() - 1] += 1;
            }
        }

        ChartInput data = new ChartInput("Total Orders", toReturn);
        return data;
    }


    ChartInput getCancelledOrders(Branch br, int month){

        LocalDate now = LocalDate.now();
        YearMonth yearMonthObject = YearMonth.of(now.getYear(), month);
        int daysInMonth = yearMonthObject.lengthOfMonth();

        int[] toReturn = new int[daysInMonth];

        List<Order> orders = br.getOrders();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate orderDate;
        System.out.println(orders.size());
        for (Order order : orders) {
            orderDate = LocalDate.parse(order.getDate(), formatter);
            if (orderDate.getMonthValue() == month) {
                if(order.getStatus().equals("CancelledByQuarantine") || order.getStatus().equals("CancelledByCustomer"))
                    toReturn[orderDate.getDayOfMonth() - 1] += 1;
            }
        }

        ChartInput data = new ChartInput("Cancelled Orders", toReturn);
        return data;
    }

    ChartInput getComplaints(Branch br, int month){

        LocalDate now = LocalDate.now();
        YearMonth yearMonthObject = YearMonth.of(now.getYear(), month);
        int daysInMonth = yearMonthObject.lengthOfMonth();

        int[] toReturn = new int[daysInMonth];

        List<Complaint> orders = br.getComplaints();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate orderDate;
        System.out.println(orders.size());
        for (Complaint order : orders) {
            orderDate = LocalDate.parse(order.getDate(), formatter);
            if (orderDate.getMonthValue() == month) {
                    toReturn[orderDate.getDayOfMonth() - 1] += 1;
            }
        }
        ChartInput data = new ChartInput("Complaints", toReturn);
        return data;
    }



    void cancelOrder(int id, ConnectionToClient client) {
        try {
            Dao<Order> orderDao = new Dao(Order.class);
            Order order = orderDao.findById(id);
            double orderPrice = order.getPrice();
            double toCharge = 0;
            String message = "";
            if (order != null) {
                String date = order.getDate(), hour = order.getHour();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate nowDate = LocalDate.parse(LocalDate.now().format(formatter), formatter);
                LocalDate orderDate = LocalDate.parse(order.getDate(), formatter);
                System.out.println(nowDate.toString());
                System.out.println(orderDate.toString());
                if (orderDate.isEqual(nowDate)) {
                    LocalTime orderHour = LocalTime.parse(hour);
                    LocalTime nowHour = LocalTime.now();
                    Duration duration = Duration.between(nowHour, orderHour);
                    long diff = duration.getSeconds() / 60;
                    System.out.println(diff);
                    if (diff > 180) {
                        toCharge = 0;
                    } else if (diff < 60) toCharge = orderPrice;
                    else {
                        toCharge = orderPrice * 0.5;
                    }
                }
                order.setStatus("CancelledByCustomer");
                orderDao.update(order);
                message = "Order " + Integer.toString(id) + " has been cancelled. You have been charged " + Double.toString(toCharge) + " shekels.";
            } else {
                message = "Order not found. Please try again.";
            }
            try {
                Warning warning = new Warning(message);
                client.sendToClient(warning);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void cancelBooking(int id, ConnectionToClient client) {
        try {
            Dao<Booking> bookDao = new Dao(Booking.class);
            System.out.println("wtf");
            Booking booking = bookDao.findById(id);
            double toCharge = 0;
            String message = "";
            if (booking != null) {
                String date = booking.getDate(), hour = booking.getTime();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate nowDate = LocalDate.parse(LocalDate.now().format(formatter), formatter);
                LocalDate orderDate = LocalDate.parse(booking.getDate(), formatter);
                System.out.println(nowDate.toString());
                System.out.println(orderDate.toString());
                if (orderDate.isEqual(nowDate)) {
                    LocalTime orderHour = LocalTime.parse(hour);
                    LocalTime nowHour = LocalTime.now();
                    Duration duration = Duration.between(nowHour, orderHour);
                    long diff = duration.getSeconds() / 60;
                    System.out.println(diff);
                    if (diff <= 60) {
                        toCharge = 10 * booking.getCustomerNum();
                    }
                }
                booking.setStatus("CancelledByCustomer");
                bookDao.update(booking);
                message = "Booking " + Integer.toString(id) + " has been cancelled. You have been charged " + Double.toString(toCharge) + " shekels.";
            } else {
                message = "Booking not found. Please try again.";
            }
            try {
                Warning warning = new Warning(message);
                client.sendToClient(warning);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void sendComplaintsToClient(int id, ConnectionToClient client) {
        Dao<Branch> branchDao = new Dao(Branch.class);
        Branch branch = branchDao.findById(id);

        List<Complaint> complaints = branch.getComplaints();

        ComplaintEvent complaintEvent = new ComplaintEvent(complaints);
        try {
            client.sendToClient(complaintEvent);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void requestMap(int branchId, String date, String hour, String area, ConnectionToClient client) {
        Dao<Branch> brDao = new Dao(Branch.class);
        Branch branch = brDao.findById(branchId);
        OccupationMap map = branch.getOccupationMap(date, hour, area);

        try {
            client.sendToClient(map);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    void addNewComplaint(Complaint complaint) {
        Dao<Complaint> complaintDao = new Dao<>(Complaint.class);
        Dao<Branch> branchDao = new Dao<>(Branch.class);
        Branch branch = complaint.getBranch();
        branch.addComplaint(complaint);
        try {
            branchDao.update(complaint.getBranch());
            // complaintDao.save(complaint);kjkjk
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void checkAvailableBooking(String[] attributes, ConnectionToClient client) {
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


    void saveBooking(Booking book, ConnectionToClient client) {
        try {
            Dao<Booking> bookDao = new Dao(Booking.class);
            try {
                bookDao.save(book);
            } catch (Exception e) {
                e.printStackTrace();
            }
            int bookId = book.getId();
            System.out.println(bookId);
            String message = "";
            if (bookDao.findById(bookId) != null) {
                message = "Booking complete! Booking ID for cancelling booking: " + bookId;
            } else {
                message = "Booking failed! Please try again.";
            }
            Warning warning = new Warning(message);
            client.sendToClient(warning);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void saveOrder(Order order, ConnectionToClient client) {
        try {
            Dao<CustomerDetails> detailsDao = new Dao(CustomerDetails.class);
            List<CustomerDetails> listOfCustomers = detailsDao.findAll();
            for (CustomerDetails customer : listOfCustomers) {
                if (customer.getName().equals(order.getCustomerDetails().getName())) {
                    order.setCustomerDetails(customer);
                    break;
                }
            }

            Branch br = order.getBr();
            br.addOrder(order);
            order.getCustomerDetails().addOrder(order);
            Dao<Branch> branchDao = new Dao(Branch.class);
            branchDao.update(br);
            Dao<Order> orderDao = new Dao(Order.class);
            int orderId = order.getId();
            String message = "";
            if (orderDao.findById(orderId) != null) {

                message = "Order complete! Order ID for cancelling order: " + orderId;
            } else {
                message = "Order failed! Please try again.";
            }
            try {
                Warning warning = new Warning(message);
                client.sendToClient(warning);
                System.out.format("Order %d added successfully\n", orderId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    void sendAllBranches(ConnectionToClient client) {
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
        } catch (Exception e) {
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

            Branch br1 = new Branch("09:00", "23:00");
            Branch br2 = new Branch("09:00", "22:00");
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

    void sendPurpleLetters(ConnectionToClient client) {
        Dao<PurpleLetter> purpleDao = new Dao(PurpleLetter.class);
        List<PurpleLetter> purples = purpleDao.findAll();
        PurpleLetterEvent event = new PurpleLetterEvent(purples);
        try {
            client.sendToClient(event);
            System.out.format("Sent warning to client %s\n", client.getInetAddress().getHostAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void updatePurpleLetter(PurpleLetter newPurpleLetter) {

        try {
            Dao<PurpleLetter> pDao = new Dao(PurpleLetter.class);
            PurpleLetter NEWp = pDao.findById(newPurpleLetter.getId());
            NEWp = newPurpleLetter;
            pDao.update(NEWp);
            cancelOrders(NEWp);
            cancelBookings(NEWp);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    void cancelOrders(PurpleLetter purpleLetter) {
        Dao<Branch> brDao = new Dao(Branch.class);
        Branch br = brDao.findById(purpleLetter.getId());
        if (purpleLetter.isQuarantine()) {
            List<Order> orders = br.getOrders();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            for (Order order : orders) {
                LocalDate orderDate = LocalDate.parse(order.getDate(), formatter);
                LocalDate qStart = LocalDate.parse(purpleLetter.getQuarantineStartDate(), formatter);
                LocalDate qEnd = LocalDate.parse(purpleLetter.getQuarantineEndDate(), formatter);
                if (orderDate.isAfter(qStart) && orderDate.isBefore(qEnd)) {
                    order.setStatus("CancelledByQuarantine");
                }
            }
            brDao.update(br);
        }
    }

    void cancelBookings(PurpleLetter purpleLetter) {
        Dao<Branch> brDao = new Dao(Branch.class);
        Branch br = brDao.findById(purpleLetter.getId());
        if (purpleLetter.isQuarantine()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate qStart = LocalDate.parse(purpleLetter.getQuarantineStartDate(), formatter);
            LocalDate qEnd = LocalDate.parse(purpleLetter.getQuarantineEndDate(), formatter);
            Mapp map = br.getMap("inside");
            if (map != null) {
                map.cancelBookings(qStart, qEnd);
            }
            map = br.getMap("outside");
            if (map != null) {
                map.cancelBookings(qStart, qEnd);
            }
            brDao.update(br);
        }
    }


}
