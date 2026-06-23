package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EquipmentSlot;

public class Parry extends EventExecutor {
   public Parry(File file) {
      super(file);
   }

   @Override
   public void damaged(int level, EntityDamageEvent event) {
      if (event.getEntity() instanceof LivingEntity) {
         LivingEntity creature = (LivingEntity)event.getEntity();
         if (creature.getEquipment() != null && creature.getEquipment().getItem(EquipmentSlot.HAND).getType().toString().contains("SWORD")) {
            event.setDamage(event.getDamage() * this.getValue("damage-multiplier", level));
         }
      }
   }
}
