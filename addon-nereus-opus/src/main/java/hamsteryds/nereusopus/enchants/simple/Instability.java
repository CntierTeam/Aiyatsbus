package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.FoliaUtils;
import hamsteryds.nereusopus.utils.MechanismUtils;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;

public class Instability extends EventExecutor {
   private static Class<?> class$ExplosionResult;
   private static Method method$ExplosionResult$valueOf;
   private static Constructor<?> constructor$BlockExplodeEvent$modern;
   private static Object enum$ExplosionResult$DESTROY;
   private static Object enum$ExplosionResult$DESTROY_WITH_DECAY;
   private static Object enum$ExplosionResult$KEEP;
   private static boolean use21 = false;

   private static Object getExplosionResult(String name) {
      try {
         return method$ExplosionResult$valueOf.invoke(null, name);
      } catch (Exception var2) {
         throw new RuntimeException("Failed to get ExplosionResult enum: " + name, var2);
      }
   }

   public Instability(File file) {
      super(file);
   }

   @Override
   public void shootBow(int level, EntityShootBowEvent event) {
      AbstractArrow arrow = (AbstractArrow)event.getProjectile();
      Location from = arrow.getLocation().clone();
      FoliaUtils.runAtFixedRate(arrow, task -> (new Runnable() {
         int counter = 10;

         @Override
         public void run() {
            if (arrow.isDead()) {
               task.cancel();
            }

            if (arrow.isInBlock()) {
               if (MechanismUtils.checkPermission(Instability.this, arrow.getLocation().getBlock(), event.getEntity())) {
                  Instability.this.explode(arrow, level);
               }

               task.cancel();
            }

            if (arrow.getLocation().distance(from) >= 100.0 || this.counter-- <= 0) {
               task.cancel();
            }
         }
      }).run(), 1L, 20L);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getDamager() instanceof AbstractArrow arrow) {
         this.explode(arrow, level);
      }
   }

   public void explode(AbstractArrow arrow, int level) {
      Location loc = arrow.getLocation();
      boolean causeFire = this.getBool("cause-fire");
      boolean breakBlocks = this.getBool("break-blocks");
      BlockExplodeEvent event;
      if (use21) {
         Object result;
         if (causeFire && breakBlocks) {
            result = enum$ExplosionResult$DESTROY;
         } else if (!causeFire && breakBlocks) {
            result = enum$ExplosionResult$DESTROY;
         } else if (causeFire && !breakBlocks) {
            result = enum$ExplosionResult$DESTROY_WITH_DECAY;
         } else {
            result = enum$ExplosionResult$KEEP;
         }

         try {
            event = (BlockExplodeEvent)constructor$BlockExplodeEvent$modern.newInstance(loc.getBlock(), loc.getBlock().getState(), new ArrayList(), 1, result);
         } catch (IllegalAccessException | InvocationTargetException | InstantiationException var9) {
            throw new RuntimeException(var9);
         }
      } else {
         event = new BlockExplodeEvent(loc.getBlock(), new ArrayList(), 1.0F);
      }

      Bukkit.getPluginManager().callEvent(event);
      if (!event.isCancelled()) {
         loc.getWorld().createExplosion(loc, (float)this.getValue("power", level), causeFire, breakBlocks);
      }

      arrow.remove();
   }

   static {
      try {
         class$ExplosionResult = Class.forName("org.bukkit.ExplosionResult");
         method$ExplosionResult$valueOf = class$ExplosionResult.getMethod("valueOf", String.class);
         constructor$BlockExplodeEvent$modern = BlockExplodeEvent.class
            .getDeclaredConstructor(Block.class, BlockState.class, List.class, float.class, class$ExplosionResult);
         enum$ExplosionResult$DESTROY = getExplosionResult("DESTROY");
         enum$ExplosionResult$DESTROY_WITH_DECAY = getExplosionResult("DESTROY_WITH_DECAY");
         enum$ExplosionResult$KEEP = getExplosionResult("KEEP");
         use21 = true;
      } catch (ClassNotFoundException | NoSuchMethodException var1) {
      }
   }
}
