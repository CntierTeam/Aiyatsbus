package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Weakening extends EventExecutor {
   public static HashMap<UUID, Long> lastAttackStamp = new HashMap<>();

   public Weakening(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getDamager() instanceof LivingEntity) {
         LivingEntity creature = (LivingEntity)event.getDamager();
         if (lastAttackStamp.containsKey(creature.getUniqueId())) {
            long stamp = lastAttackStamp.get(creature.getUniqueId());
            if (System.currentTimeMillis() - stamp <= this.getValue("duration", level) * 50.0) {
               event.setDamage(event.getDamage() * this.getValue("damage-multiplier", level));
            }
         }

         lastAttackStamp.put(creature.getUniqueId(), System.currentTimeMillis());
      }
   }
}
