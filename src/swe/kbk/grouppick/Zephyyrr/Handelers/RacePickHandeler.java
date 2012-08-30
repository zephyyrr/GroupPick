package swe.kbk.grouppick.Zephyyrr.Handelers;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import swe.kbk.grouppick.Zephyyrr.GroupPick;
import swe.kbk.grouppick.Zephyyrr.PermHandeler;

/**
 * The listener that listens for the relevant commands and if found, changes the
 * senders permission group into one of the predefined groups.
 * 
 * @author zephyyrr
 * 
 */
public class RacePickHandeler implements CommandExecutor {

	Logger log;
	GroupPick plugin;
	PermHandeler ph;

	public RacePickHandeler(GroupPick grouppick) {
		log = Logger.getLogger("Minecraft.bukkit.GroupPick");
		plugin = grouppick;
		ph = grouppick.getPermissionHandeler();
		if (plugin == null) {
			log.severe("RaceHandeler: PLUGIN IS NULL!");
		}
		if (ph == null) {
			log.severe("RaceHandeler: PERMISSIONHANDELER IS NULL!");
		}

	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		if (sender instanceof Player) { // alla CommandSenders behöver inte
			// vara Players...
			return pickRace((Player) sender, ((Player) sender).getWorld()
					.getName(), args);
		} else {
			sender.sendMessage("This command is supposed to be used by a player.");
			return false;
		}
	}	

	/**
	 * @param sender
	 */
	public void noPermsAction(CommandSender sender) {
		sender.sendMessage("You do not have access to this command!");
	}

	/**
	 * @param sender
	 * @param args
	 * @return
	 * @throws Throwable
	 */
	public boolean pickRace(Player sender, String world, String[] args) {
		if (args.length < 1) {
			sender.sendMessage("You forgot to choose a race!");
			return false;
		}

		String targetGroup = args[0].substring(0, 1).toUpperCase()
				+ args[0].substring(1).toLowerCase();

		sender.sendMessage("I have heard your wish to become a " + targetGroup
				+ ", " + ((Player) sender).getName());

		boolean res = false;

		if (hasPermission(sender, world, "GroupPick.choose.race." + targetGroup)) { // perm
			// check

			if (ph.inGroup(sender.getName(), world, targetGroup)) {
				sender.sendMessage("You have already choosen this race.");
				return true;
			}

			for (String g : plugin.getRaces()) {
				if (ph.inGroup(sender.getName(), world, g)
						&& !hasPermission(sender, world,
								"GroupPick.override.choose.race")) {
					sender.sendMessage("You have already choosen another race.");
					return true;
				}
			}

			if (plugin.getRaces().contains(targetGroup)) {
				res = ph.addGroup(sender.getName(), world, targetGroup);
			} else {
				sender.sendMessage("That race does not exist!");
				sender.sendMessage("The races are: "
						+ plugin.getRacesAsString());
				return true;
			}

			if (res) {
				sender.sendMessage("Congratulation!, You have been transformed into an "
						+ targetGroup);
				log.info("[" + plugin.getName() + "]" + sender.getName() + " have choosen to play as a "
						+ targetGroup);

				if (ph.inGroup(sender.getName(), world, plugin.getGuestGroup())) {
					res = ph.promote(sender.getName());
					if (res) {
						log.info(sender.getName()
								+ " has also been promoted to the Standard group!");
					}
				}
			} else {
				sender.sendMessage("Something went wrong during your transformation.");
				sender.sendMessage("Please use a legitimate race next time.");
				sender.sendMessage("The races are: "
						+ plugin.getRacesAsString());
			}
			return res;
		} else {
			sender.sendMessage("You do not have permission to choose that race!");
			return true;
		}
	}

	public boolean hasPermission(CommandSender user, String world, String perm) {

		return (user instanceof Player) ? ((Player) user).hasPermission(perm)
				: false;
	}

}
