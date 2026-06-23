package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.ListenerRegisterer;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Cranial extends EventExecutor implements Listener {
   public Cranial(File file) {
      super(file);
      ListenerRegisterer.register(this);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getEntity() instanceof LivingEntity) {
         LivingEntity creature = (LivingEntity)event.getEntity();
         Entity trident = event.getDamager();
         if (trident.getLocation().getY() < creature.getLocation().getY() + creature.getEyeHeight() - 0.22) {
            return;
         }

         event.setDamage(event.getDamage() * this.getValue("damage-multiplier", level));
      }
   }
}
