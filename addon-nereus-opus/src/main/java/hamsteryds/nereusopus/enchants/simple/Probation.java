package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import java.io.File;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

public class Probation extends EventExecutor implements FoliaNeeded {
   public Probation(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      Entity damager = event.getDamager();
      if (damager instanceof Projectile projectile && projectile.getShooter() instanceof LivingEntity creature) {
         damager = creature;
      }

      if (damager instanceof LivingEntity creature) {
         creature.addPotionEffect(PotionEffectType.LEVITATION.createEffect((int)this.getValue("duration", level), (int)this.getValue("amplifier", level) - 1));
      }
   }
}
