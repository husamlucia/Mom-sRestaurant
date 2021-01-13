package il.cshaifasweng.OCSFMediatorExample.client.events;

import il.cshaifasweng.OCSFMediatorExample.entities.Menu;
import il.cshaifasweng.OCSFMediatorExample.entities.MenuPOJO;

public class AllMealsEvent {
    private MenuPOJO menu;

    public AllMealsEvent(MenuPOJO menu) {
        this.menu = menu;
    }

    public MenuPOJO getMenu() {
        return menu;
    }
}
