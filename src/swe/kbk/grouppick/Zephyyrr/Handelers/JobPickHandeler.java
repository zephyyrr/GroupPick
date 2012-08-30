package swe.kbk.grouppick.Zephyyrr.Handelers;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import swe.kbk.grouppick.Zephyyrr.GroupPick;
import swe.kbk.grouppick.Zephyyrr.PermHandeler;

public class JobPickHandeler implements CommandExecutor {

	Logger log;
	GroupPick plugin;
	PermHandeler ph;

	public JobPickHandeler(GroupPick grouppick) {
		log = Logger.getLogger("Minecraft.bukkit.GroupPick");
		plugin = grouppick;
		ph = grouppick.getPermissionHandeler();
		if (plugin == null) {
			log.severe("JobHandeler: PLUGIN IS NULL!");
		}
		if (ph == null) {
			log.severe("JobHandeler: PERMISSIONHANDELER IS NULL!");
		}

	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		if (sender instanceof Player) { // alla CommandSenders behöver inte
			// vara Players...
			return pickJob((Player) sender, ((Player) sender).getWorld()
					.getName(), args);
		} else {
			sender.sendMessage("This command is supposed to be used by a player.");
			return false;
		}
	}

	private boolean pickJob(Player sender, String world, String[] args) {
		if (args.length < 1) {
			sender.sendMessage("You forgot to choose a job!");
			return false;
		}

		String targetGroup = args[0].substring(0, 1).toUpperCase()
				+ args[0].substring(1).toLowerCase();

		sender.sendMessage("I have heard your wish to become a " + targetGroup
				+ ", " + ((Player) sender).getName());

		boolean res = false;

		if (hasPermission(sender, world, "GroupPick.choose.job." + targetGroup)) { // perm
			// check

			if (ph.inGroup(sender.getName(), world, targetGroup)) {
				sender.sendMessage("You have already choosen this job.");
				return true;
			}

			for (String g : plugin.getJobs()) {
				if (ph.inGroup(sender.getName(), world, g)
						&& !hasPermission(sender, world,
								"GroupPick.override.choose.job")) {
					sender.sendMessage("You have already choosen another job.");
					return true;
				}
			}


			if (plugin.getJobs().contains(targetGroup)) {
				res = ph.addGroup(sender.getName(), world, targetGroup);
			} else {
				sender.sendMessage("That job does not exist!");
				sender.sendMessage("The jobs are: "
						+ plugin.getJobsAsString());
				return true;
			}

			if (res) {
				sender.sendMessage("Congratulation!, You have been trained into an "
						+ targetGroup);
				log.info(sender.getName() + " have choosen to play as a "
						+ targetGroup);

				if (ph.inGroup(sender.getName(), world, plugin.getGuestGroup())) {
					res = ph.promote(sender.getName());
					if (res) {
						log.info(sender.getName()
								+ " has also been promoted to the Standard group!");
					}
				}
			} else {
				sender.sendMessage("Something went wrong during your training.");
				sender.sendMessage("Please use a legitimate job next time.");
				sender.sendMessage("The jobs are: "
						+ plugin.getJobsAsString());
			}
			return res;
		} else {
			sender.sendMessage("You do not have permission to choose that job!");
			return true;
		}
	}

	public boolean hasPermission(CommandSender user, String world, String perm) {

		return (user instanceof Player) ? ((Player) user).hasPermission(perm)
				: false;
	}

}
