package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import hamsteryds.nereusopus.utils.FoliaUtils;
import hamsteryds.nereusopus.utils.InventoryUtils;
import hamsteryds.nereusopus.utils.NBTUtils;
import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataType;

public class Magnetic extends EventExecutor implements FoliaNeeded {
   private static boolean only_sneak = false;

   public Magnetic(File file) {
      super(file);
      only_sneak = this.getBool("only-on-sneak", false);
   }

   @Override
   public void tickTask(int level, EquipmentSlot slot, Player player, int stamp) {
      if (!only_sneak || player.isSneaking()) {
         double range = this.getValue("range", level);
         FoliaUtils.runTask(player, task -> {
            for (Entity entity : player.getNearbyEntities(range, range, range)) {
               if (entity instanceof Item drop) {
                  if (!this.getBool("softmode", false)) {
                     if (!NBTUtils.has("thrown", drop.getPersistentDataContainer(), PersistentDataType.INTEGER) && drop.canPlayerPickup()) {
                        PlayerAttemptPickupItemEvent evt = new PlayerAttemptPickupItemEvent(player, drop);
                        Bukkit.getPluginManager().callEvent(evt);
                        if (!evt.isCancelled()) {
                           InventoryUtils.giveItemOrDrop(player, drop.getItemStack());
                           drop.remove();
                           break;
                        }
                     }
                  } else if (!NBTUtils.has("thrown", drop.getPersistentDataContainer(), PersistentDataType.INTEGER)) {
                     FoliaUtils.teleport(drop, player);
                  }
               }

               if (entity instanceof ExperienceOrb orb) {
                  FoliaUtils.teleport(orb, player);
               }
            }
         });
      }
   }
}
