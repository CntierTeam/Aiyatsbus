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

public class Abrasion extends EventExecutor implements FoliaNeeded {
   public Abrasion(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getEntity() instanceof LivingEntity creature) {
         Map<EquipmentSlot, ItemStack> equipped = InventoryUtils.getEquippedItems(creature);

         for (EquipmentSlot slot : equipped.keySet()) {
            creature.getEquipment().setItem(slot, ItemUtils.addDurability(equipped.get(slot), this.getValue("durability-decrease", level)));
         }
      }
   }
}
