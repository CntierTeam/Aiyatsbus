package hamsteryds.nereusopus.enchants.simple;

import cc.polarastrum.aiyatsbus.core.compat.GuardItemChecker;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.FoliaUtils;
import java.io.File;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Telekinesis extends EventExecutor {
   private final int checkRadius = this.getInt("check-radius", 2);
   private final int checkDelay = this.getInt("check-delay", 1);

   public Telekinesis(File file) {
      super(file);
   }

   @Override
   public void blockBreak(int level, BlockBreakEvent event) {
      Player player = event.getPlayer();
      Location block = event.getBlock().getLocation();
      FoliaUtils.runTask(player, t -> {
         player.giveExp(event.getExpToDrop(), true);
         this.pickNearItems(player, block);
      });
      event.setExpToDrop(0);
   }

   @Override
   public void kill(int level, EntityDeathEvent event) {
      int droppedExp = event.getDroppedExp();
      if (event.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent evt) {
         Location dead = evt.getEntity().getLocation();
         if (evt.getDamager() instanceof Projectile projectile) {
            if (projectile.getShooter() instanceof Player player) {
               FoliaUtils.runTask(player, t -> {
                  player.giveExp(droppedExp, true);
                  this.pickNearItems(player, dead);
               });
               event.setDroppedExp(0);
            }
         } else if (evt.getDamager() instanceof Player player) {
            FoliaUtils.runTask(player, t -> {
               player.giveExp(droppedExp, true);
               this.pickNearItems(player, dead);
            });
            event.setDroppedExp(0);
         }
      }
   }

   private boolean canFitItem(Player player, ItemStack item) {
      Inventory inventory = player.getInventory();
      int emptySlots = 0;
      int availableSpace = 0;

      for (ItemStack stack : inventory.getStorageContents()) {
         if (stack == null || stack.getType() == Material.AIR) {
            emptySlots++;
            availableSpace += item.getMaxStackSize();
         } else if (stack.isSimilar(item)) {
            availableSpace += item.getMaxStackSize() - stack.getAmount();
         }
      }

      boolean canFit = emptySlots > 0 || availableSpace >= item.getAmount();
      if (canFit) {
         inventory.addItem(new ItemStack[]{item});
      }

      return canFit;
   }

   private void pickNearItems(Player player, Location block) {
      FoliaUtils.runDelayed(block, task -> {
         for (Item item : block.getNearbyEntitiesByType(Item.class, this.checkRadius)) {
            if (!this.isQuickShopGuardItem(item, player) && this.canFitItem(player, item.getItemStack())) {
               item.remove();
            }
         }
      }, (long)this.checkDelay);
   }

   private boolean isQuickShopGuardItem(Item item, Player player) {
      return GuardItemChecker.Companion.checkIsGuardItem(item, player);
   }
}
