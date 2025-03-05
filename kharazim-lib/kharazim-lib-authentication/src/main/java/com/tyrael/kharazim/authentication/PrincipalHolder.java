package com.tyrael.kharazim.authentication;

/**
 * @author Tyrael Archangel
 * @since 2025/3/5
 */
public class PrincipalHolder {

    private static final ThreadLocal<Principal> PRINCIPAL = new ThreadLocal<>();

    @SuppressWarnings("unchecked")
    public static <T extends Principal> T getPrincipal() {
        Principal principal = PRINCIPAL.get();
        return principal == null ? null : (T) principal;
    }

    public static <T extends Principal> void setPrincipal(T principal) {
        PRINCIPAL.set(principal);
    }

    public static void remove() {
        PRINCIPAL.remove();
    }

}
