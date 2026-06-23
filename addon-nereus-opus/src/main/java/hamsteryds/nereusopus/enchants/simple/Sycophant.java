package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import java.io.File;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Sycophant extends EventExecutor implements FoliaNeeded {
   public Sycophant(File file) {
      super(file);
   }

   @Override
   public void damagedByEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getEntity() instanceof Player) {
         Player player = (Player)event.getEntity();
         if (player.isBlocking()) {
            player.setHealth(Math.min(player.getHealth() + event.getDamage() * this.getValue("percent", level) / 100.0, player.getMaxHealth()));
            player.setCooldown(Material.SHIELD, (int)(this.getValue("cooldown", level) * 20.0));
         }
      }
   }
}
