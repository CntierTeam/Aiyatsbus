package hamsteryds.nereusopus.utils;

import hamsteryds.nereusopus.ConfigReader;
import hamsteryds.nereusopus.NereusOpus;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TeamUtils {
   public static final boolean teamDamageEnabled = ConfigReader.teamEnableDamage;
   public static final String placeHolder = ConfigReader.teamPlaceholder;

   public static void initialize() {
      NereusOpus.getInstance().getLang().log.info("Team Damage Enable : " + (teamDamageEnabled ? "true" : "false"));
   }

   public static void addMember(String name, UUID memberId) {
      NereusOpus.teamDataManager.addMember(name, memberId);
   }

   public static void addMember(String name, Player member) {
      addMember(name, member.getUniqueId());
   }

   public static void createTeam(String name) {
      NereusOpus.teamDataManager.createTeam(name);
   }

   public static void removeTeam(String name) {
      NereusOpus.teamDataManager.removeTeam(name);
   }

   public static void removeMember(String name, UUID memberId) {
      NereusOpus.teamDataManager.removeMember(name, memberId);
   }

   public static void removeMember(String name, Player member) {
      removeMember(name, member.getUniqueId());
   }

   public static void removeMember(UUID id) {
      removeMember(getTeamName(id), id);
   }

   public static void removeMember(Player p) {
      removeMember(p.getUniqueId());
   }

   public static String getTeamName(UUID id) {
      return NereusOpus.teamDataManager.getTeamName(id);
   }

   public static String getTeamName(Player p) {
      return getTeamName(p.getUniqueId());
   }

   public static Set<UUID> getMembers(String name) {
      return NereusOpus.teamDataManager.getMembers(name);
   }

   public static Set<String> getMemberNames(String name) {
      Set<String> result = new HashSet<>();

      for (UUID id : NereusOpus.teamDataManager.getMembers(name)) {
         result.add(Objects.requireNonNull(Bukkit.getOfflinePlayer(id)).getName());
      }

      return result;
   }

   public static boolean isTeammate(UUID id1, UUID id2) {
      return getTeamName(id1) != null && getTeamName(id2) != null ? getTeamName(id1).equals(getTeamName(id2)) : false;
   }

   public static Set<String> getAllTeams() {
      return NereusOpus.teamDataManager.getTeams();
   }

   public static boolean isTeammate(Player p1, Player p2) {
      return isTeammate(p1.getUniqueId(), p2.getUniqueId());
   }

   public static boolean isTeamExist(String name) {
      return NereusOpus.teamDataManager.isTeamExist(name);
   }
}
