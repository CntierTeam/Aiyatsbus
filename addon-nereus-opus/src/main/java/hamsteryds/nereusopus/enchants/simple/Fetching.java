package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.ListenerRegisterer;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.FoliaUtils;
import hamsteryds.nereusopus.utils.InventoryUtils;
import hamsteryds.nereusopus.utils.ItemUtils;
import java.io.File;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class Fetching extends EventExecutor implements Listener {
   public Fetching(File file) {
      super(file);
      ListenerRegisterer.register(this);
   }

   @Override
   public void kill(int level, EntityDeathEvent event) {
      if (this.getBool("avaliable-on-players") || !(event.getEntity() instanceof Player)) {
         if (((EntityDamageByEntityEvent)event.getEntity().getLastDamageCause()).getDamager() instanceof Wolf wolf && wolf.getOwner() instanceof Player) {
            Player player = (Player)wolf.getOwner();
            ItemStack item = player.getInventory().getHelmet();
            if (item != null && ItemUtils.getEnchants(item).containsKey(this)) {
               List<ItemStack> drops = event.getDrops();
               FoliaUtils.runTask(event.getEntity(), t -> {
                  for (ItemStack drop : event.getDrops()) {
                     InventoryUtils.giveItemOrDrop(player, drop);
                  }

                  player.giveExp(event.getDroppedExp());
               });
               drops.clear();
               event.setDroppedExp(0);
            }
         }
      }
   }
}
