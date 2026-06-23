package hamsteryds.nereusopus.enchants.simple;

import cc.polarastrum.aiyatsbus.core.AiyatsbusUtilsKt;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

public class Chopless extends EventExecutor {
   public Chopless(File file) {
      super(file);
   }

   @Override
   public void damagedByEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getDamager() instanceof LivingEntity) {
         LivingEntity creature = (LivingEntity)event.getDamager();
         if (creature.getEquipment() != null
            && AiyatsbusUtilsKt.aiyatsbusTarget("axes").getTypes().contains(creature.getEquipment().getItem(EquipmentSlot.HAND).getType())) {
            event.setDamage(event.getDamage() * this.getValue("damage-multiplier", level));
         }
      }
   }
}
