package hamsteryds.nereusopus.enchants.simple;

import taboolib.library.xseries.XPotion;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import hamsteryds.nereusopus.utils.FoliaUtils;
import hamsteryds.nereusopus.utils.ItemUtils;
import hamsteryds.nereusopus.utils.PermissionUtils;
import java.io.File;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

public class Dejecting extends EventExecutor implements FoliaNeeded {
   public Dejecting(File file) {
      super(file);
   }

   @Override
   public void tickTask(int level, EquipmentSlot slot, Player player, int stamp) {
      FoliaUtils.runTask(player, task -> {
         double range = this.getValue("range", level);
         int duration = (int)this.getValue("duration", level);
         PotionEffect effect = XPotion.SLOWNESS.buildPotionEffect(duration, (int)this.getValue("amplifier", level) - 1);
         boolean flag = false;

         for (Entity entity : player.getNearbyEntities(range, range, range)) {
            if (entity instanceof LivingEntity creature && !PermissionUtils.checkIfIsNPC(entity) && PermissionUtils.hasDamagePermission(player, creature)) {
               creature.addPotionEffect(effect);
               flag = true;
            }
         }

         if (Math.random() <= 0.2 && flag) {
            PlayerInventory inv = player.getInventory();
            ItemStack item = inv.getItem(slot);
            inv.setItem(slot, ItemUtils.addDurability(item, 1));
         }
      });
   }
}
