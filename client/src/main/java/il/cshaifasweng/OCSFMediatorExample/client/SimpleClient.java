package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import org.greenrobot.eventbus.EventBus;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;

public class SimpleClient extends AbstractClient {
	
	private static SimpleClient client = null;

	private SimpleClient(String host, int port) {
		super(host, port);
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		if (msg.getClass().equals(Warning.class)) {
			EventBus.getDefault().post(new WarningEvent((Warning) msg));
		}
		else if(msg.getClass().equals(Menu.class)){
			EventBus.getDefault().post(new MenuEvent((Menu) msg));
		}
		else if(msg.getClass().equals(MenuPOJO.class)){
			EventBus.getDefault().post(new AllMealsEvent((MenuPOJO) msg));
		}
		else if(msg.getClass().equals(BranchList.class)){
			EventBus.getDefault().post(new BranchEvent((BranchList) msg));
		}
	}
	
	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("localhost", 3000);
		}
		return client;
	}

}
