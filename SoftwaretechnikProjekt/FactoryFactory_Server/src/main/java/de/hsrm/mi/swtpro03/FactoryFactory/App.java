package de.hsrm.mi.swtpro03.FactoryFactory;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class App {

	public static void main(String args[]){
		String defaultConfig = "config.xml";
		String stubconfig = "stubconfig.xml";
		String [] resources = null;
		
		if(args.length > 0){
			if(args[0].equalsIgnoreCase("-stub")){
				resources = new String[] {stubconfig};
			}else{
				String customDataSource = "file:" + System.getProperty("user.dir") + "/" + args[0];
				resources = new String[] {defaultConfig, customDataSource};
			}
		}
		else{
			System.out.println("Zu wenige Parameter! Datenbank config.xml vergessen?");
		}

		final ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(resources);
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			
			@Override
			public void run() {
				applicationContext.destroy();
				
			}
		}));
	}

	private App(){	}
}