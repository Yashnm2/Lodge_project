import java.util.*;

public class Helper {

    // ==================== CHARACTER METHODS ====================

    /**
     * Check if a character is a vowel (case-insensitive)
     */
    public static boolean isVowel(char c) {
        c = Character.toLowerCase(c);
        return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u';
    }

    /**
     * Check if a character is a consonant (case-insensitive)
     */
    public static boolean isConsonant(char c) {
        return isAlphabet(c) && !isVowel(c);
    }

    /**
     * Check if a character is alphabetic (A-Z or a-z)
     */
    public static boolean isAlphabet(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    /**
     * Check if a character is alphanumeric (A-Z, a-z, or 0-9)
     */
    public static boolean isAlphanumeric(char c) {
        return isAlphabet(c) || isDigit(c);
    }

    /**
     * Check if a character is a digit (0-9)
     */
    public static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }


    // ==================== STRING METHODS ====================

    /**
     * Reverse a string
     */
    public static String reverseString(String str) {
        return new StringBuilder(str).reverse().toString();
    }

    /**
     * Check if a string is a palindrome (case-sensitive)
     */
    public static boolean isPalindrome(String str) {
        return str.equals(reverseString(str));
    }

    /**
     * Check if a string is a palindrome (case-insensitive)
     */
    public static boolean isPalindromeCaseInsensitive(String str) {
        String lower = str.toLowerCase();
        return lower.equals(reverseString(lower));
    }

    /**
     * Count occurrences of a character in a string
     */
    public static int countChar(String str, char c) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }

    /**
     * Count vowels in a string
     */
    public static int countVowels(String str) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (isVowel(str.charAt(i))) {
                count++;
            }
        }
        return count;
    }

    /**
     * Count consonants in a string
     */
    public static int countConsonants(String str) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (isConsonant(str.charAt(i))) {
                count++;
            }
        }
        return count;
    }

    /**
     * Check if a string contains only digits
     */
    public static boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if a string contains only alphabetic characters
     */
    public static boolean isAlphabetic(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!isAlphabet(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if a string contains only alphanumeric characters
     */
    public static boolean isAlphanumericString(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!isAlphanumeric(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Extract all digits from a string and return as a string
     */
    public static String extractDigits(String str) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (isDigit(c)) {
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * Extract all letters from a string and return as a string
     */
    public static String extractLetters(String str) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (isAlphabet(c)) {
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * Count occurrences of a substring in a string
     */
    public static int countSubstring(String str, String substring) {
        int count = 0;
        int index = 0;
        while ((index = str.indexOf(substring, index)) != -1) {
            count++;
            index += substring.length();
        }
        return count;
    }

    // ==================== ARRAY METHODS ====================

    /**
     * Reverse an integer array (in-place)
     */
    public static void reverseArray(int[] arr) {
        int left = 0;
        int right = arr.length - 1;
        while (left < right) {
            int temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;
            left++;
            right--;
        }
    }

    /**
     * Reverse a String array (in-place)
     */
    public static void reverseArray(String[] arr) {
        int left = 0;
        int right = arr.length - 1;
        while (left < right) {
            String temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;
            left++;
            right--;
        }
    }

    /**
     * Reverse a double array (in-place)
     */
    public static void reverseArray(double[] arr) {
        int left = 0;
        int right = arr.length - 1;
        while (left < right) {
            double temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;
            left++;
            right--;
        }
    }

    /**
     * Find the maximum value in an integer array
     */
    public static int findMax(int[] arr) {
        if (arr.length == 0) {
            throw new IllegalArgumentException("Array is empty");
        }
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }

    /**
     * Find the minimum value in an integer array
     */
    public static int findMin(int[] arr) {
        if (arr.length == 0) {
            throw new IllegalArgumentException("Array is empty");
        }
        int min = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < min) {
                min = arr[i];
            }
        }
        return min;
    }

    /**
     * Calculate the sum of an integer array
     */
    public static int sum(int[] arr) {
        int total = 0;
        for (int num : arr) {
            total += num;
        }
        return total;
    }

    /**
     * Calculate the average of an integer array
     */
    public static double average(int[] arr) {
        if (arr.length == 0) {
            throw new IllegalArgumentException("Array is empty");
        }
        return (double) sum(arr) / arr.length;
    }

    /**
     * Check if an array contains a specific value
     */
    public static boolean contains(int[] arr, int value) {
        for (int num : arr) {
            if (num == value) {
                return true;
            }
        }
        return false;
    }

    /**
     * Print an array
     */
    public static void printArray(int[] arr) {
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i < arr.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }

    /**
     * Print a String array
     */
    public static void printArray(String[] arr) {
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i < arr.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }

    /**
     * Find index of an element in an integer array
     * Returns -1 if not found
     */
    public static int indexOf(int[] arr, int value) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == value) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Find index of an element in a String array
     * Returns -1 if not found
     */
    public static int indexOf(String[] arr, String value) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(value)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Count occurrences of a value in an integer array
     */
    public static int countOccurrences(int[] arr, int value) {
        int count = 0;
        for (int num : arr) {
            if (num == value) {
                count++;
            }
        }
        return count;
    }

    /**
     * Remove duplicates from an integer array and return new array
     */
    public static int[] removeDuplicates(int[] arr) {
        List<Integer> list = new ArrayList<>();
        for (int num : arr) {
            if (!list.contains(num)) {
                list.add(num);
            }
        }
        int[] result = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }

    /**
     * Convert integer array to ArrayList
     */
    public static ArrayList<Integer> arrayToList(int[] arr) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int num : arr) {
            list.add(num);
        }
        return list;
    }

    /**
     * Convert ArrayList to integer array
     */
    public static int[] listToArray(ArrayList<Integer> list) {
        int[] arr = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }


    // ==================== LIST METHODS (Integer) ====================

    /**
     * Remove duplicates from an Integer list (preserves order)
     */
    public static List<Integer> removeDuplicatesListInteger(List<Integer> list) {
        List<Integer> result = new ArrayList<>();
        for (Integer item : list) {
            if (!result.contains(item)) {
                result.add(item);
            }
        }
        return result;
    }

    /**
     * Find common elements between two Integer lists
     */
    public static List<Integer> findCommonElementsInteger(List<Integer> list1, List<Integer> list2) {
        List<Integer> result = new ArrayList<>();
        for (Integer item : list1) {
            if (list2.contains(item) && !result.contains(item)) {
                result.add(item);
            }
        }
        return result;
    }

    /**
     * Check if an Integer list contains duplicates
     */
    public static boolean hasDuplicatesInteger(List<Integer> list) {
        Set<Integer> set = new HashSet<>(list);
        return set.size() != list.size();
    }

    /**
     * Count occurrences of an element in an Integer list
     */
    public static int countOccurrencesListInteger(List<Integer> list, Integer element) {
        int count = 0;
        for (Integer item : list) {
            if (item.equals(element)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Find maximum value in an Integer list
     */
    public static int findMaxList(List<Integer> list) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("List is empty");
        }
        int max = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i) > max) {
                max = list.get(i);
            }
        }
        return max;
    }

    /**
     * Find minimum value in an Integer list
     */
    public static int findMinList(List<Integer> list) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("List is empty");
        }
        int min = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i) < min) {
                min = list.get(i);
            }
        }
        return min;
    }


    // ==================== LIST METHODS (Double) ====================

    /**
     * Remove duplicates from a Double list (preserves order)
     */
    public static List<Double> removeDuplicatesListDouble(List<Double> list) {
        List<Double> result = new ArrayList<>();
        for (Double item : list) {
            if (!result.contains(item)) {
                result.add(item);
            }
        }
        return result;
    }

    /**
     * Find maximum value in a Double list
     */
    public static double findMaxListDouble(List<Double> list) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("List is empty");
        }
        double max = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i) > max) {
                max = list.get(i);
            }
        }
        return max;
    }

    /**
     * Find minimum value in a Double list
     */
    public static double findMinListDouble(List<Double> list) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("List is empty");
        }
        double min = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i) < min) {
                min = list.get(i);
            }
        }
        return min;
    }


    // ==================== LIST METHODS (String) ====================

    /**
     * Remove duplicates from a String list (preserves order)
     */
    public static List<String> removeDuplicatesListString(List<String> list) {
        List<String> result = new ArrayList<>();
        for (String item : list) {
            if (!result.contains(item)) {
                result.add(item);
            }
        }
        return result;
    }

    /**
     * Find common elements between two String lists
     */
    public static List<String> findCommonElementsString(List<String> list1, List<String> list2) {
        List<String> result = new ArrayList<>();
        for (String item : list1) {
            if (list2.contains(item) && !result.contains(item)) {
                result.add(item);
            }
        }
        return result;
    }

    /**
     * Count occurrences of an element in a String list
     */
    public static int countOccurrencesListString(List<String> list, String element) {
        int count = 0;
        for (String item : list) {
            if (item.equals(element)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Check if a String list contains an element (case-insensitive)
     */
    public static boolean containsIgnoreCase(List<String> list, String element) {
        for (String item : list) {
            if (item.equalsIgnoreCase(element)) {
                return true;
            }
        }
        return false;
    }


    // ==================== NUMBER METHODS ====================

    /**
     * Check if a number is prime
     */
    public static boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        }
        if (n == 2) {
            return true;
        }
        if (n % 2 == 0) {
            return false;
        }
        for (int i = 3; i <= Math.sqrt(n); i += 2) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Calculate factorial of a number
     */
    public static long factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Factorial not defined for negative numbers");
        }
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    /**
     * Calculate GCD (Greatest Common Divisor) of two numbers
     */
    public static int gcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    /**
     * Calculate LCM (Least Common Multiple) of two numbers
     */
    public static int lcm(int a, int b) {
        if (a == 0 || b == 0) {
            return 0;
        }
        return Math.abs(a * b) / gcd(a, b);
    }

    /**
     * Get the nth Fibonacci number (0-indexed)
     */
    public static long fibonacci(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Index must be non-negative");
        }
        if (n <= 1) {
            return n;
        }
        long prev = 0;
        long curr = 1;
        for (int i = 2; i <= n; i++) {
            long next = prev + curr;
            prev = curr;
            curr = next;
        }
        return curr;
    }


    // ==================== UTILITY METHODS ====================

    /**
     * Swap two elements in an array
     */
    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /**
     * Create a deep copy of an integer array
     */
    public static int[] copyArray(int[] arr) {
        int[] copy = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            copy[i] = arr[i];
        }
        return copy;
    }


    // ==================== CONVERSION & PARSING METHODS ====================

    /**
     * Parse integer from string, return default value if invalid
     */
    public static int parseIntOrDefault(String str, int defaultValue) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Parse double from string, return default value if invalid
     */
    public static double parseDoubleOrDefault(String str, double defaultValue) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Check if a string can be parsed as an integer
     */
    public static boolean isValidInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Check if a string can be parsed as a double
     */
    public static boolean isValidDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    // ==================== DOUBLE ARRAY METHODS ====================

    /**
     * Find the maximum value in a double array
     */
    public static double findMaxDouble(double[] arr) {
        if (arr.length == 0) {
            throw new IllegalArgumentException("Array is empty");
        }
        double max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }

    /**
     * Find the minimum value in a double array
     */
    public static double findMinDouble(double[] arr) {
        if (arr.length == 0) {
            throw new IllegalArgumentException("Array is empty");
        }
        double min = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < min) {
                min = arr[i];
            }
        }
        return min;
    }

    /**
     * Calculate the sum of a double array
     */
    public static double sumDouble(double[] arr) {
        double total = 0;
        for (double num : arr) {
            total += num;
        }
        return total;
    }

    /**
     * Calculate the average of a double array
     */
    public static double averageDouble(double[] arr) {
        if (arr.length == 0) {
            throw new IllegalArgumentException("Array is empty");
        }
        return sumDouble(arr) / arr.length;
    }

    /**
     * Check if a double array contains a specific value
     */
    public static boolean containsDouble(double[] arr, double value) {
        for (double num : arr) {
            if (num == value) {
                return true;
            }
        }
        return false;
    }

    /**
     * Print a double array
     */
    public static void printArrayDouble(double[] arr) {
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i < arr.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }


    // ==================== STRING ARRAY METHODS ====================

    /**
     * Check if a String array contains a specific value (case-sensitive)
     */
    public static boolean containsString(String[] arr, String value) {
        for (String str : arr) {
            if (str.equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if a String array contains a specific value (case-insensitive)
     */
    public static boolean containsStringIgnoreCase(String[] arr, String value) {
        for (String str : arr) {
            if (str.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Count occurrences of a value in a String array
     */
    public static int countOccurrencesString(String[] arr, String value) {
        int count = 0;
        for (String str : arr) {
            if (str.equals(value)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Convert String array to ArrayList
     */
    public static ArrayList<String> stringArrayToList(String[] arr) {
        ArrayList<String> list = new ArrayList<>();
        for (String str : arr) {
            list.add(str);
        }
        return list;
    }

    /**
     * Convert ArrayList<String> to String array
     */
    public static String[] stringListToArray(ArrayList<String> list) {
        String[] arr = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }


    // ==================== MORE STRING METHODS ====================

    /**
     * Remove all non-alphanumeric characters from a string
     */
    public static String removeNonAlphanumeric(String str) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (isAlphanumeric(c)) {
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * Remove all whitespace from a string
     */
    public static String removeWhitespace(String str) {
        return str.replaceAll("\\s+", "");
    }

    /**
     * Capitalize first letter of a string
     */
    public static String capitalizeFirst(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    /**
     * Convert string to title case (first letter of each word capitalized)
     */
    public static String toTitleCase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        String[] words = str.split("\\s+");
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            if (words[i].length() > 0) {
                result.append(Character.toUpperCase(words[i].charAt(0)));
                result.append(words[i].substring(1).toLowerCase());
                if (i < words.length - 1) {
                    result.append(" ");
                }
            }
        }
        return result.toString();
    }


    // ==================== MATH UTILITY METHODS ====================

    /**
     * Round to specified decimal places
     */
    public static double round(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }

    /**
     * Clamp a value between min and max
     */
    public static int clamp(int value, int min, int max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    /**
     * Check if a number is within a range (inclusive)
     */
    public static boolean inRange(int value, int min, int max) {
        return value >= min && value <= max;
    }
}
