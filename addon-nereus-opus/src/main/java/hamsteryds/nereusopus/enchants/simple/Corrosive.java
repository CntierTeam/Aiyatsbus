package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import hamsteryds.nereusopus.utils.InventoryUtils;
import hamsteryds.nereusopus.utils.ItemUtils;
import java.io.File;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;

public class Corrosive extends EventExecutor implements FoliaNeeded {
   public Corrosive(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getEntity() instanceof LivingEntity) {
         LivingEntity creature = (LivingEntity)event.getEntity();
         EntityEquipment equip = creature.getEquipment();
         if (equip == null) {
            return;
         }

         for (EquipmentSlot slot : InventoryUtils.getEquippedItems(creature).keySet()) {
            equip.setItem(slot, ItemUtils.addDurability(equip.getItem(slot), (short)this.getValue("durability-decrease", level)));
         }
      }
   }
}
