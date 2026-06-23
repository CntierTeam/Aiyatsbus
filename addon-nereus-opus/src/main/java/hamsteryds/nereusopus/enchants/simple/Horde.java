package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.FoliaUtils;
import hamsteryds.nereusopus.utils.stats.MathUtils;
import java.io.File;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Horde extends EventExecutor {
   public Horde(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      FoliaUtils.runTask(event.getEntity(), task -> {
         double range = this.getValue("range", level);
         int amount = 0;

         for (Entity entity : event.getEntity().getNearbyEntities(range, range, range)) {
            if (entity instanceof LivingEntity && !(entity instanceof ArmorStand)) {
               amount++;
            }
         }

         event.setDamage(event.getDamage() * MathUtils.calculate(this.getText("damage-multiplier"), "level", level, "amount", amount));
      });
   }
}
