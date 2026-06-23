package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import hamsteryds.nereusopus.utils.InventoryUtils;
import hamsteryds.nereusopus.utils.ItemUtils;
import java.io.File;
import java.util.Map;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class Grit extends EventExecutor implements FoliaNeeded {
   public Grit(File file) {
      super(file);
   }

   @Override
   public void damagedByEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getDamager() instanceof LivingEntity creature) {
         Map<EquipmentSlot, ItemStack> equipped = InventoryUtils.getEquippedItems(creature);

         for (EquipmentSlot slot : equipped.keySet()) {
            creature.getEquipment().setItem(slot, ItemUtils.addDurability(equipped.get(slot), this.getValue("durability-decrease", level)));
         }
      }
   }
}
