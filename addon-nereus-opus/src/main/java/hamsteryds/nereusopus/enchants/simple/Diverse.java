package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.InventoryUtils;
import java.io.File;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class Diverse extends EventExecutor {
   public Diverse(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      ItemStack item = InventoryUtils.getEquippedItems(event.getEntity()).get(EquipmentSlot.HAND);
      if (item.getType().toString().contains("SWORD")) {
         event.setDamage(event.getDamage() * this.getValue("damage-multiplier", level));
      }
   }
}
