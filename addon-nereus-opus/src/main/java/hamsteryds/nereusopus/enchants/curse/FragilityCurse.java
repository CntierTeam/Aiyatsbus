package hamsteryds.nereusopus.enchants.curse;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class FragilityCurse extends EventExecutor {
   public FragilityCurse(File file) {
      super(file);
   }

   @Override
   public void itemDamage(int level, PlayerItemDamageEvent event) {
      event.setDamage((int)(event.getDamage() + this.getValue("durability-decrease-extra", level)));
   }
}
