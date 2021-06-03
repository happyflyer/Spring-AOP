package org.example.spring_aop.teach.security;

/**
 * @author lifei
 */
public class CurrentUserHolder {
    public static final ThreadLocal<String> holder = new ThreadLocal<>();

    public static String get() {
        return holder.get() == null ? "unknown" : holder.get();
    }

    public static void set(String user) {
        holder.set(user);
    }
}
