package hamsteryds.nereusopus.utils;

import taboolib.library.configuration.ConfigurationSection;
import taboolib.module.configuration.ConfigFile;
import hamsteryds.nereusopus.NereusOpus;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ConfigUtils {
   public static <T extends Enum<T>> List<T> getEnumList(ConfigurationSection section, String fileName, String path, Class<T> clazz, String... defaultValues) {
      List<String> values = section.getStringList(path);
      List<T> results = new ArrayList<>();

      for (String value : values) {
         try {
            results.add(Enum.valueOf(clazz, value.toUpperCase(Locale.ROOT)));
         } catch (IllegalArgumentException var10) {
            NereusOpus.getInstance()
               .getLang()
               .log
               .error(
                  "\u2716 NereusOpus\u914d\u7f6e\u51fa\u73b0\u9519\u8bef "
                     + fileName
                     + " : "
                     + path
                     + " - "
                     + value
                     + " \u503c\u9519\u8bef\uff0c\u5df2\u81ea\u52a8\u5c06\u5176\u5ffd\u7565\uff01"
               );
         }
      }

      return results;
   }

   public static <T> List<T> getObjectList(ConfigurationSection section, String fileName, String path, Class<T> clazz, String... defaultValues) {
      List<String> values = section.getStringList(path);
      List<T> results = new ArrayList<>();

      for (String value : values) {
         try {
            Object obj = clazz.getDeclaredMethod("fromId", String.class).invoke(null, value);
            results.add((T)obj);
         } catch (IllegalArgumentException var10) {
            NereusOpus.getInstance()
               .getLang()
               .log
               .error(
                  "\u2716 NereusOpus\u914d\u7f6e\u51fa\u73b0\u9519\u8bef "
                     + fileName
                     + " : "
                     + path
                     + " - "
                     + value
                     + " \u503c\u9519\u8bef\uff0c\u5df2\u81ea\u52a8\u5c06\u5176\u5ffd\u7565\uff01"
               );
         } catch (Exception var11) {
         }
      }

      return results;
   }

   public static Map<String, String> getKeyMap(ConfigFile yaml, String sectionPath) {
      Map<String, String> result = new HashMap<>();
      ConfigurationSection section = yaml.getConfigurationSection(sectionPath);

      for (String path : section.getKeys(true)) {
         if (section.getString(path) != null) {
            result.put(path, section.getString(path));
         }
      }

      return result;
   }

   public static <K, V> Map<K, V> getMapFromList(ConfigurationSection section, String path, String splitSymbol, Class<K> keyClazz, Class<V> valueClazz) {
      if (section == null) {
         return new HashMap<>();
      } else {
         List<String> list = section.getStringList(path);
         Map<K, V> result = new HashMap<>();

         for (String line : list) {
            result.put(getObjectFromString(line.split(splitSymbol)[0], keyClazz), getObjectFromString(line.split(splitSymbol)[1], valueClazz));
         }

         return result;
      }
   }

   public static <T> T getObjectFromString(String string, Class<T> clazz) {
      try {
         if (Enum.class.isAssignableFrom(clazz)) {
            Class<? extends Enum> enumClass = clazz.asSubclass(Enum.class);
            return (T)Enum.valueOf(enumClass, string.toUpperCase(Locale.ROOT));
         } else if (Number.class.isAssignableFrom(clazz) || Boolean.class.isAssignableFrom(clazz)) {
            Method parseMethod = clazz.getDeclaredMethod("parse" + clazz.getSimpleName(), String.class);
            return (T)parseMethod.invoke(null, string);
         } else if (clazz.isAssignableFrom(String.class)) {
            return (T)string;
         } else {
            Method method = clazz.getDeclaredMethod("fromId", String.class);
            return (T)method.invoke(null, string);
         }
      } catch (Exception var3) {
         var3.printStackTrace();
         return null;
      }
   }
}
