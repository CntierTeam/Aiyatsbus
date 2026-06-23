package hamsteryds.nereusopus.utils;

import hamsteryds.nereusopus.NereusOpus;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;

public class FoliaUtils {
   public static void teleport(Entity from, Entity to) {
      teleport(from, to.getLocation());
   }

   public static void teleport(Entity from, Location to) {
      if (NereusOpus.getInstance().isFolia()) {
         from.teleportAsync(to);
      } else {
         from.teleport(to);
      }
   }

   public static FoliaUtils.MixedTask runAtFixedRate(FoliaUtils.MixedTask.CancellableTask runnable, long delay, long period) {
      if (delay <= 0L) {
         delay = 1L;
      }

      if (period <= 0L) {
         period = 1L;
      }

      FoliaUtils.MixedTask task = new FoliaUtils.MixedTask(runnable);
      if (NereusOpus.getInstance().isFolia()) {
         Bukkit.getGlobalRegionScheduler().runAtFixedRate(NereusOpus.getInstance(), scheduledTask -> task.setFoliaTask(scheduledTask).run(), delay, period);
      } else {
         Bukkit.getScheduler().runTaskTimer(NereusOpus.getInstance(), bukkitTask -> task.setBukkitTask(bukkitTask).run(), delay, period);
      }

      return task;
   }

   public static FoliaUtils.MixedTask runAtFixedRate(Block block, FoliaUtils.MixedTask.CancellableTask runnable, long delay, long period) {
      return runAtFixedRate(block.getLocation(), runnable, delay, period);
   }

   public static FoliaUtils.MixedTask runAtFixedRate(Location location, FoliaUtils.MixedTask.CancellableTask runnable, long delay, long period) {
      if (delay <= 0L) {
         delay = 1L;
      }

      if (period <= 0L) {
         period = 1L;
      }

      FoliaUtils.MixedTask task = new FoliaUtils.MixedTask(runnable);
      if (NereusOpus.getInstance().isFolia()) {
         Bukkit.getRegionScheduler().runAtFixedRate(NereusOpus.getInstance(), location, scheduledTask -> task.setFoliaTask(scheduledTask).run(), delay, period);
      } else {
         Bukkit.getScheduler().runTaskTimer(NereusOpus.getInstance(), bukkitTask -> task.setBukkitTask(bukkitTask).run(), delay, period);
      }

      return task;
   }

   public static FoliaUtils.MixedTask runAtFixedRate(Entity entity, FoliaUtils.MixedTask.CancellableTask runnable, long delay, long period) {
      if (delay <= 0L) {
         delay = 1L;
      }

      if (period <= 0L) {
         period = 1L;
      }

      FoliaUtils.MixedTask task = new FoliaUtils.MixedTask(runnable);
      if (NereusOpus.getInstance().isFolia()) {
         entity.getScheduler().runAtFixedRate(NereusOpus.getInstance(), scheduledTask -> task.setFoliaTask(scheduledTask).run(), null, delay, period);
      } else {
         Bukkit.getScheduler().runTaskTimer(NereusOpus.getInstance(), bukkitTask -> task.setBukkitTask(bukkitTask).run(), delay, period);
      }

      return task;
   }

   public static FoliaUtils.MixedTask runTask(FoliaUtils.MixedTask.CancellableTask runnable) {
      FoliaUtils.MixedTask task = new FoliaUtils.MixedTask(runnable);
      if (NereusOpus.getInstance().isFolia()) {
         Bukkit.getGlobalRegionScheduler().run(NereusOpus.getInstance(), scheduledTask -> task.setFoliaTask(scheduledTask).run());
      } else {
         Bukkit.getScheduler().runTask(NereusOpus.getInstance(), bukkitTask -> task.setBukkitTask(bukkitTask).run());
      }

      return task;
   }

   public static FoliaUtils.MixedTask runTask(Block block, FoliaUtils.MixedTask.CancellableTask runnable) {
      return runTask(block.getLocation(), runnable);
   }

   public static FoliaUtils.MixedTask runTask(Location location, FoliaUtils.MixedTask.CancellableTask runnable) {
      FoliaUtils.MixedTask task = new FoliaUtils.MixedTask(runnable);
      if (NereusOpus.getInstance().isFolia()) {
         Bukkit.getRegionScheduler().run(NereusOpus.getInstance(), location, scheduledTask -> task.setFoliaTask(scheduledTask).run());
      } else {
         Bukkit.getScheduler().runTask(NereusOpus.getInstance(), bukkitTask -> task.setBukkitTask(bukkitTask).run());
      }

      return task;
   }

   public static FoliaUtils.MixedTask runTask(Entity entity, FoliaUtils.MixedTask.CancellableTask runnable) {
      FoliaUtils.MixedTask task = new FoliaUtils.MixedTask(runnable);
      if (NereusOpus.getInstance().isFolia()) {
         entity.getScheduler().run(NereusOpus.getInstance(), scheduledTask -> task.setFoliaTask(scheduledTask).run(), null);
      } else {
         Bukkit.getScheduler().runTask(NereusOpus.getInstance(), bukkitTask -> task.setBukkitTask(bukkitTask).run());
      }

      return task;
   }

   public static FoliaUtils.MixedTask runDelayed(FoliaUtils.MixedTask.CancellableTask runnable, long delay) {
      if (delay <= 0L) {
         delay = 1L;
      }

      FoliaUtils.MixedTask task = new FoliaUtils.MixedTask(runnable);
      if (NereusOpus.getInstance().isFolia()) {
         Bukkit.getGlobalRegionScheduler().runDelayed(NereusOpus.getInstance(), scheduledTask -> task.setFoliaTask(scheduledTask).run(), delay);
      } else {
         Bukkit.getScheduler().runTaskLater(NereusOpus.getInstance(), bukkitTask -> task.setBukkitTask(bukkitTask).run(), delay);
      }

      return task;
   }

   public static FoliaUtils.MixedTask runDelayed(Block block, FoliaUtils.MixedTask.CancellableTask runnable, long delay) {
      return runDelayed(block.getLocation(), runnable, delay);
   }

   public static FoliaUtils.MixedTask runDelayed(Location location, FoliaUtils.MixedTask.CancellableTask runnable, long delay) {
      if (delay <= 0L) {
         delay = 1L;
      }

      FoliaUtils.MixedTask task = new FoliaUtils.MixedTask(runnable);
      if (NereusOpus.getInstance().isFolia()) {
         Bukkit.getRegionScheduler().runDelayed(NereusOpus.getInstance(), location, scheduledTask -> task.setFoliaTask(scheduledTask).run(), delay);
      } else {
         Bukkit.getScheduler().runTaskLater(NereusOpus.getInstance(), bukkitTask -> task.setBukkitTask(bukkitTask).run(), delay);
      }

      return task;
   }

   public static FoliaUtils.MixedTask runDelayed(Entity entity, FoliaUtils.MixedTask.CancellableTask runnable, long delay) {
      if (delay <= 0L) {
         delay = 1L;
      }

      FoliaUtils.MixedTask task = new FoliaUtils.MixedTask(runnable);
      if (NereusOpus.getInstance().isFolia()) {
         entity.getScheduler().runDelayed(NereusOpus.getInstance(), scheduledTask -> task.setFoliaTask(scheduledTask).run(), null, delay);
      } else {
         Bukkit.getScheduler().runTaskLater(NereusOpus.getInstance(), bukkitTask -> task.setBukkitTask(bukkitTask).run(), delay);
      }

      return task;
   }

   public static class MixedTask implements Runnable {
      private final FoliaUtils.MixedTask.CancellableTask delegate;
      @Nullable
      private BukkitTask taskB;
      @Nullable
      private ScheduledTask taskF;

      public MixedTask(FoliaUtils.MixedTask.CancellableTask delegate) {
         this.delegate = delegate;
      }

      public FoliaUtils.MixedTask setBukkitTask(BukkitTask task) {
         this.taskB = task;
         return this;
      }

      public FoliaUtils.MixedTask setFoliaTask(ScheduledTask task) {
         this.taskF = task;
         return this;
      }

      @Override
      public void run() {
         this.delegate.run(this);
      }

      public void cancel() {
         if (this.taskB != null) {
            this.taskB.cancel();
         }

         if (this.taskF != null) {
            this.taskF.cancel();
         }
      }

      @FunctionalInterface
      public interface CancellableTask {
         void run(FoliaUtils.MixedTask var1);
      }
   }
}
