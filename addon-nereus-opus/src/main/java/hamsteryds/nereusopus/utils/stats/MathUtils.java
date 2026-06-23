package hamsteryds.nereusopus.utils.stats;

import hamsteryds.nereusopus.utils.StringUtils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class MathUtils {
   private static final int[] operatePriority = new int[]{0, 3, 2, 1, -1, 1, 0, 2};
   private static final List<String> symbols = Arrays.asList(">=", "<=", ">", "<", "=", "!=");

   public static boolean isTrue(String expression) {
      for (String symbol : symbols) {
         if (expression.contains(symbol)) {
            String[] splited = expression.split(symbol);
            switch (symbol) {
               case ">=":
                  return calculate(splited[0]) >= calculate(splited[1]);
               case "<=":
                  return calculate(splited[0]) <= calculate(splited[1]);
               case ">":
                  return calculate(splited[0]) > calculate(splited[1]);
               case "<":
                  return calculate(splited[0]) < calculate(splited[1]);
               case "=":
                  return calculate(splited[0]) == calculate(splited[1]);
               case "!=":
                  return calculate(splited[0]) != calculate(splited[1]);
               default:
                  return true;
            }
         }
      }

      return true;
   }

   @SafeVarargs
   public static <E> double calculate(String expression, E... params) {
      expression = StringUtils.replace(expression, params);
      expression = expression.replace(" ", "");
      return MathUtils.Calculator.calculate(MathUtils.Calculator.transform(expression + "+0"));
   }

   public static double calculate(String expression, Map<String, String> params) {
      expression = StringUtils.replace(expression, params);
      expression = expression.replace(" ", "");
      return MathUtils.Calculator.calculate(MathUtils.Calculator.transform(expression + "+0"));
   }

   public static String numToRoman(int number, boolean ignoreI) {
      StringBuilder rNumber = new StringBuilder();
      int[] aArray = new int[]{1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
      String[] rArray = new String[]{"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
      if (number >= 1 && number <= 3999) {
         for (int i = 0; i < aArray.length; i++) {
            while (number >= aArray[i]) {
               rNumber.append(rArray[i]);
               number -= aArray[i];
            }
         }
      } else {
         rNumber = new StringBuilder("-1");
      }

      return rNumber.toString().equalsIgnoreCase("I") && ignoreI ? "" : rNumber.toString();
   }

   private static class Calculator {
      public static String transform(String expression) {
         char[] arr = expression.toCharArray();

         for (int i = 0; i < arr.length; i++) {
            if (arr[i] == '-') {
               if (i == 0) {
                  arr[i] = '~';
               } else {
                  char c = arr[i - 1];
                  if (c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == 'E' || c == 'e') {
                     arr[i] = '~';
                  }
               }
            }
         }

         if (arr[0] != '~' && arr[1] != '(') {
            return new String(arr);
         } else {
            arr[0] = '-';
            return "0" + new String(arr);
         }
      }

      public static double calculate(String expression) {
         Stack<String> postfixStack = new Stack<>();
         Stack<Character> opStack = new Stack<>();
         Stack<String> resultStack = new Stack<>();
         prepare(expression, postfixStack, opStack);
         Collections.reverse(postfixStack);

         while (!postfixStack.isEmpty()) {
            String currentValue = postfixStack.pop();
            if (!isOperator(currentValue.charAt(0))) {
               currentValue = currentValue.replace("~", "-");
               resultStack.push(currentValue);
            } else {
               String secondValue = resultStack.pop();
               String firstValue = resultStack.pop();
               firstValue = firstValue.replace("~", "-");
               secondValue = secondValue.replace("~", "-");
               String tempResult = calculate(firstValue, secondValue, currentValue.charAt(0));
               resultStack.push(tempResult);
            }
         }

         return Double.parseDouble(resultStack.pop());
      }

      public static void prepare(String expression, Stack<String> postfixStack, Stack<Character> opStack) {
         opStack.push(',');
         char[] arr = expression.toCharArray();
         int currentIndex = 0;
         int count = 0;

         for (int i = 0; i < arr.length; i++) {
            char currentOp = arr[i];
            if (isOperator(currentOp)) {
               if (count > 0) {
                  postfixStack.push(new String(arr, currentIndex, count));
               }

               char peekOp = opStack.peek();
               if (currentOp == ')') {
                  while (opStack.peek() != '(') {
                     postfixStack.push(String.valueOf(opStack.pop()));
                  }

                  opStack.pop();
               } else {
                  while (currentOp != '(' && peekOp != ',' && compare(currentOp, peekOp)) {
                     postfixStack.push(String.valueOf(opStack.pop()));
                     peekOp = opStack.peek();
                  }

                  opStack.push(currentOp);
               }

               count = 0;
               currentIndex = i + 1;
            } else {
               count++;
            }
         }

         if (count > 1 || count == 1 && !isOperator(arr[currentIndex])) {
            postfixStack.push(new String(arr, currentIndex, count));
         }

         while (opStack.peek() != ',') {
            postfixStack.push(String.valueOf(opStack.pop()));
         }
      }

      public static boolean isOperator(char c) {
         return c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')';
      }

      public static boolean compare(char cur, char peek) {
         return MathUtils.operatePriority[peek - '('] >= MathUtils.operatePriority[cur - '('];
      }

      public static String calculate(String firstValue, String secondValue, char currentOp) {
         switch (currentOp) {
            case '*':
               return String.valueOf(MathUtils.Calculator.ArithHelper.mul(firstValue, secondValue));
            case '+':
               return String.valueOf(MathUtils.Calculator.ArithHelper.add(firstValue, secondValue));
            case ',':
            case '.':
            default:
               return "";
            case '-':
               return String.valueOf(MathUtils.Calculator.ArithHelper.sub(firstValue, secondValue));
            case '/':
               return String.valueOf(MathUtils.Calculator.ArithHelper.div(firstValue, secondValue));
         }
      }

      public static class ArithHelper {
         public static final int DEF_DIV_SCALE = 16;

         public static double add(String v1, String v2) {
            BigDecimal b1 = new BigDecimal(v1);
            BigDecimal b2 = new BigDecimal(v2);
            return b1.add(b2).doubleValue();
         }

         public static double sub(String v1, String v2) {
            BigDecimal b1 = new BigDecimal(v1);
            BigDecimal b2 = new BigDecimal(v2);
            return b1.subtract(b2).doubleValue();
         }

         public static double mul(String v1, String v2) {
            BigDecimal b1 = new BigDecimal(v1);
            BigDecimal b2 = new BigDecimal(v2);
            return b1.multiply(b2).doubleValue();
         }

         public static double div(String v1, String v2) {
            BigDecimal b1 = new BigDecimal(v1);
            BigDecimal b2 = new BigDecimal(v2);
            return b1.divide(b2, 16, RoundingMode.HALF_UP).doubleValue();
         }
      }
   }
}
