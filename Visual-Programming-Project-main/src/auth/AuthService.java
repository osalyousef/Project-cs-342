package auth;

public class AuthService {
    private String email = "asdasda";
    private String pwd = "13123";

    public AuthService() {

    }

    public boolean checkLogin(String e, String p) {
        if (this.email.equals(e)) {
            if (this.pwd.equals(p)) {
                return true;
            }
        }
        return false;
    }
}
