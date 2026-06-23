package hamsteryds.nereusopus.enchants.simple;

import taboolib.library.xseries.particles.XParticle;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.FoliaUtils;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Volatile extends EventExecutor {
   private static Class<?> class$ExplosionResult;
   private static Method method$ExplosionResult$valueOf;
   private static Constructor<?> constructor$BlockExplodeEvent$modern;
   private static Object enum$ExplosionResult$KEEP;
   private static boolean use21 = false;

   private static Object getExplosionResult(String name) {
      try {
         return method$ExplosionResult$valueOf.invoke(null, name);
      } catch (Exception var2) {
         throw new RuntimeException("Failed to get ExplosionResult enum: " + name, var2);
      }
   }

   public Volatile(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getDamager() instanceof Player player) {
         this.explode(event.getEntity().getLocation(), level, player);
      }
   }

   public void explode(Location loc, int level, Player player) {
      BlockExplodeEvent event;
      if (use21) {
         try {
            event = (BlockExplodeEvent)constructor$BlockExplodeEvent$modern.newInstance(
               loc.getBlock(), loc.getBlock().getState(), new ArrayList(), 1, enum$ExplosionResult$KEEP
            );
         } catch (IllegalAccessException | InvocationTargetException | InstantiationException var6) {
            throw new RuntimeException(var6);
         }
      } else {
         event = new BlockExplodeEvent(loc.getBlock(), new ArrayList(), 1.0F);
      }

      Bukkit.getPluginManager().callEvent(event);
      if (!event.isCancelled()) {
         this.createExplosionParticle(loc);
         FoliaUtils.runTask(loc, task -> {
            for (Entity entity : loc.getNearbyEntities(3.0, 3.0, 3.0)) {
               if (entity != player && entity instanceof Damageable damageable) {
                  damageable.damage(new Random().nextInt(5) + 4);
               }
            }
         });
      }
   }

   private void createExplosionParticle(Location location) {
      location.getWorld().spawnParticle(XParticle.EXPLOSION_EMITTER.get(), location, 1);
   }

   static {
      try {
         class$ExplosionResult = Class.forName("org.bukkit.ExplosionResult");
         method$ExplosionResult$valueOf = class$ExplosionResult.getMethod("valueOf", String.class);
         constructor$BlockExplodeEvent$modern = BlockExplodeEvent.class
            .getDeclaredConstructor(Block.class, BlockState.class, List.class, float.class, class$ExplosionResult);
         enum$ExplosionResult$KEEP = getExplosionResult("KEEP");
         use21 = true;
      } catch (ClassNotFoundException | NoSuchMethodException var1) {
      }
   }
}
