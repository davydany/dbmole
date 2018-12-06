package dbmole.cli;

public class CLILogger {

    public void log(String startColor, String msg) {
        System.out.println(
            String.format("%s[DBMOLE] - %s%s", startColor, msg, ConsoleColors.RESET));
    }

    public void logError(String startColor, String msg) {
        System.err.println(
                String.format("%s[DBMOLE] - %s%s", startColor, msg, ConsoleColors.RESET));
    }

    public void info(String msg) {
        log(ConsoleColors.CYAN, msg);
    }

    public void debug(String msg) {
        log(ConsoleColors.PURPLE, msg);
    }

    public void error(String msg) {
        logError(ConsoleColors.RED_BOLD_BRIGHT, msg);
    }

    public void warning(String msg) {
        log(ConsoleColors.YELLOW, msg);
    }

    public void exception(String msg, Exception ex) {
        logError(ConsoleColors.RED_BOLD_BRIGHT, msg);
        ex.printStackTrace();
        System.err.println(ConsoleColors.RESET);
    }

    public void ok(String msg) {
        log(ConsoleColors.GREEN, "[  OK  ] " + msg);
    }

    public void fail(String msg) {
        log(ConsoleColors.RED, "[ FAIL ] " + msg);
    }

    public static CLILogger getInstance() {
        return new CLILogger();
    }
}
