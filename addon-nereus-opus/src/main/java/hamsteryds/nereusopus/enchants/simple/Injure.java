package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.stats.Pair;
import java.io.File;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Injure extends EventExecutor {
   public static HashMap<UUID, Pair<Long, Integer>> stamps = new HashMap<>();

   public Injure(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getEntity() instanceof LivingEntity creature) {
         if (stamps.containsKey(creature.getUniqueId())) {
            level = Math.max(level, stamps.get(creature.getUniqueId()).getSecond());
         }

         stamps.put(creature.getUniqueId(), new Pair<>(System.currentTimeMillis(), level));
      }
   }
}
