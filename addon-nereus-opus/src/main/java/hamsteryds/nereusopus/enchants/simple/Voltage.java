package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.InventoryUtils;
import hamsteryds.nereusopus.utils.stats.MathUtils;
import java.io.File;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;

public class Voltage extends EventExecutor {
   public Voltage(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getEntity() instanceof LivingEntity) {
         LivingEntity creature = (LivingEntity)event.getEntity();
         EntityEquipment equip = creature.getEquipment();
         int amount = 0;
         if (equip != null) {
            for (EquipmentSlot slot : InventoryUtils.ARMORS) {
               Material type = equip.getItem(slot).getType();
               if (type.toString().contains("IRON") || type.toString().contains("GOLD")) {
                  amount++;
               }
            }
         }

         event.setDamage(event.getDamage() * MathUtils.calculate(this.getText("damage-multiplier"), "level", level, "amount", amount));
      }
   }
}
