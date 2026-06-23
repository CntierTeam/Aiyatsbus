package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import java.io.File;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Confusion extends EventExecutor implements FoliaNeeded {
   public Confusion(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getEntity() instanceof Player) {
         Player player = (Player)event.getEntity();
         PlayerInventory inv = player.getInventory();
         ItemStack main = inv.getItemInMainHand();
         ItemStack off = inv.getItemInOffHand();
         inv.setItemInMainHand(off);
         inv.setItemInOffHand(main);
      }
   }
}
