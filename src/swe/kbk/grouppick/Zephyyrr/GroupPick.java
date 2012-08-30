package swe.kbk.grouppick.Zephyyrr;

/*
 * Importing bukkit stuff.
 */
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import swe.kbk.grouppick.Zephyyrr.Handelers.AdminHandeler;
import swe.kbk.grouppick.Zephyyrr.Handelers.JobPickHandeler;
import swe.kbk.grouppick.Zephyyrr.Handelers.RacePickHandeler;
import swe.kbk.grouppick.Zephyyrr.Handelers.SecretHandeler;

/*
 * Importing permission plugins.
 */
//import ru.tehkode.permissions.bukkit.PermissionsEx;
//import com.nijikokun.bukkit.Permissions.Permissions;

/*
 * Importing good stuff.
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * Man ska kommentera ordentligt Williamsson... Annars kanske någon snor din
 * credit... Okai..
 * 
 * @author Williamsson
 * @author Zephyyrr
 * @version 3.0.0
 * 
 */
public class GroupPick extends JavaPlugin implements Runnable {
	public static GroupPick plugin;

	public final Logger logger = Logger.getLogger("Minecraft");
	private PermHandeler permissionHandler;
	//private Plugin permissionsPlugin;

	//private List<String> groups;
	byte retries;

	/**
	 * This is the constructor. Here, you instantiate all variables, but then
	 * leave them alone to be set up in the onEnable() method.
	 */
	public GroupPick() {
		retries = 0;
	}

	/**
	 * This is run when disabling the plugin. This needs to revert all variables
	 * to its original state.
	 */
	@Override
	public void onDisable() {
		permissionHandler = null;
		this.logger.info("GroupPick disabled!");
	}

	/**
	 * This method is run when the plugin is enabled. This means that anything
	 * needed is to be set up in here
	 */
	@Override
	public void onEnable() {
		boolean permExists = setupPermissions();

		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " version " + pdfFile.getVersion()
				+ " is enabled!");

		//Fixing the configuration-file
		getConfig().options().copyDefaults(true);
		saveConfig();

		if (!permExists) {
			logger.severe("The permissions plugin has not been loaded yet.");
			if (retries < 5) {
				logger.info("Waiting 100 (~10sec) ticks until retry...");
				retries++;
				this.getServer().getScheduler().scheduleAsyncDelayedTask(this, this, 100L);
			} else {
				logger.info("Retried five times, giving up.");
				this.getPluginLoader().disablePlugin(this);
			}


		}

		RacePickHandeler rph = new RacePickHandeler(this);
		JobPickHandeler jph = new JobPickHandeler(this);
		AdminHandeler adh = new AdminHandeler(this);

		getCommand("racepick").setExecutor(rph);
		getCommand("jobpick").setExecutor(jph);
		getCommand("groupadmin").setExecutor(adh);
		getCommand("supersecretstuff").setExecutor(new SecretHandeler(this));
		
		System.out.println(getJobsAsString());
		System.out.println(getRacesAsString());
	}

	private boolean setupPermissions() {
		if (permissionHandler != null) {
			return true;
		}

		boolean pluginfound = false;

		if (this.getServer() != null) {
			ArrayList<String> permPlugins = new ArrayList<String>();
			addPermPlugins2List(permPlugins);
			pluginfound = this.getServer().getPluginManager().isPluginEnabled("PermissionsEx");
			if (pluginfound) {
				permissionHandler = new PEXHandeler(logger);
			} 
			if (!pluginfound) {
				pluginfound = this.getServer().getPluginManager().isPluginEnabled("Permissions");

				if (pluginfound) {
					//permissionHandler = new P3xHandeler(this, logger);
				}
			}
		}

		if (permissionHandler != null) {
			logger.info("Found and will use plugin "
					+ (permissionHandler.toString()));
			return true;

		}
		logger.info("pluginfound status: " + pluginfound);
		logger.warning("Permission system not detected, disabeling plugin again.");
		return false;

	}

	private void addPermPlugins2List(List<String> permPlugins) {
		permPlugins.add("PermissionsEX");
		//permPlugins.add("Permissions");
	}

	public List<String> getRaces() {
		return getConfig().getStringList("Groups" + getConfig().options().pathSeparator() + "Races");
	}

	public String getRacesAsString() {
		return getListAsString(getRaces());
	}

	/**
	 * @return
	 */
	public String getListAsString(List<String> list) {
		StringBuilder sb = new StringBuilder();
		Iterator<String> it = list.iterator();
		while (it.hasNext()) {
			String g = it.next();
			if (it.hasNext()) {
				sb.append(g + ", ");
			} else {
				sb.append("and " + g);
			}
		}
		return sb.toString();
	}

	public String getGuestGroup() {
		return getConfig().getString("GuestGroup", permissionHandler.getGuestGroup());
	}

	public PermHandeler getPermissionHandeler() {
		return permissionHandler;
	}

	@Deprecated
	public Plugin getPermissionsPlugin() {
		//return permissionsPlugin;
		return null;
	}

	public boolean groupExists(String group) {
		Collection<String> groups_t = permissionHandler.getGroups(Bukkit.getServer().getWorlds().get(0).getName());
		return groups_t.contains(group);
	}

	public String getStandardGroup() {
		return getConfig().getString("StandardGroup", "Standard");
	}

	@Override
	public void run() {
		this.onDisable();
		this.onEnable();
	}

	public List<String> getJobs() {
		return getConfig().getStringList("Groups" + getConfig().options().pathSeparator() + "Jobs");
	}

	public String getJobsAsString() {
		return getListAsString(getJobs());
	}

}