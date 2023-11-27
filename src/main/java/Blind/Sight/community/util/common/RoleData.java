package Blind.Sight.community.util.common;

public enum RoleData {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER"),
    SYSTEM("ROLE_PRE");

    private final String role;

    RoleData(String role){
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
