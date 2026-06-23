package hamsteryds.nereusopus.utils.team;

import hamsteryds.nereusopus.ConfigReader;
import hamsteryds.nereusopus.NereusOpus;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class FileTeamDataManager extends TeamDataManagerAPI {
   private static final int SAVE_THRESHOLD = ConfigReader.teamSaveThreshold;
   private static final Map<String, Set<UUID>> CACHE_TEAMS = new ConcurrentHashMap<>();
   private static final Map<UUID, String> CACHE_NAME = new ConcurrentHashMap<>();
   private static FileConfiguration data;
   private static String dataPath;
   private static int setsSinceLastSave = 0;

   FileTeamDataManager() {
      CACHE_TEAMS.clear();
      CACHE_TEAMS.clear();
      dataPath = NereusOpus.getInstance().getDataFolder() + "/" + ConfigReader.teamSavePath;
      File dataFile = new File(dataPath);
      if (!dataFile.exists()) {
         NereusOpus.getInstance()
            .getLang()
            .log
            .warn(
               "\u2716 NereusOpus\u7ec4\u961f\u6570\u636e\u9519\u8bef : "
                  + dataPath
                  + " \u4e0d\u5b58\u5728\uff0c\u5df2\u4fdd\u5b58\u81f3\u9ed8\u8ba4\u8def\u5f84\uff01"
            );
         dataPath = NereusOpus.getInstance().getDataFolder() + "team/data.yml";
         NereusOpus.getInstance().saveResource("team/data.yml", false);
      }
   }

   @Override
   public void save() {
      try {
         File dataFile = new File(dataPath);
         data.save(dataFile);
      } catch (IOException var2) {
         NereusOpus.getInstance().getLang().log.error("\u2716 NereusOpus\u7ec4\u961f\u6570\u636e\u4fdd\u5b58\u9519\u8bef : " + var2);
         var2.printStackTrace();
      }
   }

   @Override
   public void load() {
      File dataFile = new File(dataPath);
      data = YamlConfiguration.loadConfiguration(dataFile);

      for (String key : data.getKeys(false)) {
         String name = data.getString(key);
         UUID id = null;

         try {
            id = UUID.fromString(key);
         } catch (Exception var8) {
            NereusOpus.getInstance()
               .getLang()
               .log
               .error("\u2716 NereusOpus\u7ec4\u961f\u6570\u636e\u9519\u8bef : " + key + " \u4e0d\u662f\u5408\u6cd5\u7684 UUID \uff01");
            continue;
         }

         CACHE_TEAMS.putIfAbsent(name, Collections.synchronizedSet(new HashSet<>()));
         CACHE_TEAMS.get(name).add(id);
         CACHE_NAME.put(id, name);
      }
   }

   @Override
   public Set<String> getTeams() {
      return CACHE_TEAMS.keySet();
   }

   @Override
   public Set<UUID> getMembers(String name) {
      return CACHE_TEAMS.get(name);
   }

   @Override
   public String getTeamName(UUID id) {
      return CACHE_NAME.get(id);
   }

   @Override
   public boolean isTeamExist(String name) {
      return CACHE_TEAMS.containsKey(name);
   }

   @Override
   public void addMember(String name, UUID id) {
      CACHE_NAME.put(id, name);
      CACHE_TEAMS.get(name).add(id);
      synchronized (FileTeamDataManager.class) {
         data.set(id.toString(), name);
      }

      setsSinceLastSave++;
      if (SAVE_THRESHOLD > 0 && setsSinceLastSave >= SAVE_THRESHOLD) {
         this.save();
         setsSinceLastSave = 0;
      }
   }

   @Override
   public void createTeam(String name) {
      CACHE_TEAMS.putIfAbsent(name, Collections.synchronizedSet(new HashSet<>()));
      setsSinceLastSave++;
      if (SAVE_THRESHOLD > 0 && setsSinceLastSave >= SAVE_THRESHOLD) {
         this.save();
         setsSinceLastSave = 0;
      }
   }

   @Override
   public void removeTeam(String name) {
      for (UUID id : CACHE_TEAMS.get(name)) {
         CACHE_NAME.remove(id);
         synchronized (FileTeamDataManager.class) {
            data.set(id.toString(), null);
         }
      }

      CACHE_TEAMS.remove(name);
      setsSinceLastSave++;
      if (SAVE_THRESHOLD > 0 && setsSinceLastSave >= SAVE_THRESHOLD) {
         this.save();
         setsSinceLastSave = 0;
      }
   }

   @Override
   public void removeMember(String name, UUID id) {
      CACHE_TEAMS.get(name).remove(id);
      CACHE_NAME.remove(id);
      synchronized (FileTeamDataManager.class) {
         data.set(id.toString(), null);
      }

      setsSinceLastSave++;
      if (SAVE_THRESHOLD > 0 && setsSinceLastSave >= SAVE_THRESHOLD) {
         this.save();
         setsSinceLastSave = 0;
      }
   }
}
