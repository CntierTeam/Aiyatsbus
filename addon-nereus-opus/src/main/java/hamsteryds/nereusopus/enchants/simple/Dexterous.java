package hamsteryds.nereusopus.enchants.simple;

import taboolib.library.xseries.XAttribute;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import java.io.File;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class Dexterous extends EventExecutor implements FoliaNeeded {
   public Dexterous(File file) {
      super(file);
   }

   @Override
   public void itemHeld(int level, PlayerItemHeldEvent event) {
      Player player = event.getPlayer();
      player.getAttribute((Attribute)XAttribute.ATTACK_SPEED.get()).setBaseValue(4.0 * (1.0 + this.getValue("cooldown-decreased", level) / 100.0));
   }
}
