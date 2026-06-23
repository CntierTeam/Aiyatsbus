package hamsteryds.nereusopus.utils.team;

import java.util.Set;
import java.util.UUID;

public abstract class TeamDataManagerAPI {
   public static synchronized TeamDataManagerAPI getInstance() {
      return new FileTeamDataManager();
   }

   public abstract void save();

   public abstract void load();

   public abstract void addMember(String var1, UUID var2);

   public abstract void createTeam(String var1);

   public abstract void removeTeam(String var1);

   public abstract void removeMember(String var1, UUID var2);

   public abstract Set<UUID> getMembers(String var1);

   public abstract String getTeamName(UUID var1);

   public abstract Set<String> getTeams();

   public abstract boolean isTeamExist(String var1);
}
