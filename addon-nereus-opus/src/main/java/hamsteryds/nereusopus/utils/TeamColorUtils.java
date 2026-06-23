package hamsteryds.nereusopus.utils;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class TeamColorUtils {
   private static final HashMap<ChatColor, Team> coloredTeams = new HashMap<>();

   public static Team getTeamByColor(ChatColor color) {
      return coloredTeams.get(color);
   }

   static {
      ScoreboardManager manager = Bukkit.getScoreboardManager();
      Scoreboard board = manager.getMainScoreboard();

      for (ChatColor color : ChatColor.values()) {
         if (board.getTeam("NO-" + color.name()) == null) {
            Team team = board.registerNewTeam("NO-" + color.name());
            team.setColor(color);
         }

         coloredTeams.put(color, board.getTeam("NO-" + color.name()));
      }
   }
}
