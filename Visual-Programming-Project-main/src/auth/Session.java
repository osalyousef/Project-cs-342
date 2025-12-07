package auth;

public class Session {
    private static int userId;
    private static String username;
    private static String email;
    private static String password;
    private static String role;

    public static void setUser(int id, String name, String mail, String pass, String r) {
        userId = id;
        username = name;
        email = mail;
        password = pass;
        role = r;
    }

    public static int getUserId() {
        return userId;
    }

    public static String getUsername() {
        return username;
    }

    public static String getEmail() {
        return email;
    }

    public static String getPassword() {
        return password;
    }

    public static String getRole() {
        return role;
    }
}
