package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.FoliaUtils;
import java.io.File;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffectType;

public class Graceful extends EventExecutor {
   public Graceful(File file) {
      super(file);
   }

   @Override
   public void damaged(int level, EntityDamageEvent event) {
      if (event.getEntity() instanceof LivingEntity) {
         LivingEntity creature = (LivingEntity)event.getEntity();
         if (event.getCause() == DamageCause.FALL) {
            FoliaUtils.runTask(creature, t -> creature.addPotionEffect(PotionEffectType.SLOW_FALLING.createEffect(200, 9)));
            event.setCancelled(true);
         }
      }
   }
}
