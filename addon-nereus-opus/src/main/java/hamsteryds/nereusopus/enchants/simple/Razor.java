package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Razor extends EventExecutor {
   public Razor(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      double multiplier = this.getValue("damage-multiplier", level);
      if (this.getBool("decrease-if-cooldown") && event.getDamager() instanceof Player) {
         Player player = (Player)event.getDamager();
         multiplier = 1.0 + (multiplier - 1.0) * player.getAttackCooldown();
      }

      if (!this.getBool("disable-on-players") || !(event.getEntity() instanceof Player)) {
         event.setDamage(event.getDamage() * multiplier);
      }
   }
}
