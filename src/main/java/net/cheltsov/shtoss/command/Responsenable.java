package net.cheltsov.shtoss.command;

/**
 * Marker interface used by <tt>Command</tt> implementations to indicate that
 * they need javax.servlet.http.HttpServletResponse object to implement execute method
 * (to add cookies for example)
 */
public interface Responsenable {
}
