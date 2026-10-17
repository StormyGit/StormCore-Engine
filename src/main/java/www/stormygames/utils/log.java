package www.stormygames.utils;

import www.stormygames.GameConfig;

public final class log {

    private static final String RESET  = "\u001B[0m";
    private static final String RED    = "\u001B[31m";
    private static final String GREEN  = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE   = "\u001B[34m";

    private log() {}

    // Información del sistema
    public static void LOG_INFO_SYS(String str) {
        if (GameConfig.SHOW_LOGS)
            System.out.println(BLUE + "[SYS] " + RESET + str);
    }

    // Información normal
    public static void LOG_INFO(AccessType type, int id, String str) {
        if (GameConfig.SHOW_LOGS)
            System.out.println(GREEN + "[INFO-"+type.name()+"-"+id+"] " + RESET + str);
    }
    // Información normal
    public static void LOG_INFO(String str) {
        if (GameConfig.SHOW_LOGS)
            System.out.println(GREEN + "[INFO] " + RESET + str);
    }

    // Advertencias
    public static void LOG_WARNING(String str) {
        if (GameConfig.SHOW_LOGS)
            System.out.println(YELLOW + "[WARNING] " + str + RESET);
    }

    // Errores
    public static void LOG_ERROR(String str) {
        if (GameConfig.SHOW_LOGS)
            System.err.println(RED + "[ERROR] " + str + RESET);
    }
}