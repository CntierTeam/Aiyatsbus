package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import java.io.File;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

public class Identify extends EventExecutor implements FoliaNeeded {
   public Identify(File file) {
      super(file);
   }

   @Override
   public void damagedByEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getEntity() instanceof Player && event.getDamager() instanceof LivingEntity) {
         Player player = (Player)event.getEntity();
         LivingEntity creature = (LivingEntity)event.getDamager();
         if (player.isBlocking()) {
            double duration = this.getValue("duration", level);
            creature.addPotionEffect(PotionEffectType.GLOWING.createEffect((int)duration, 0));
         }
      }
   }
}
