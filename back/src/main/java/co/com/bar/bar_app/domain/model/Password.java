package co.com.bar.bar_app.domain.model;

public class Password {
    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    private final String value;

    private Password(String value) {
        this.value = value;
    }

    public static Password fromPlain(String plain) {
        if (!plain.matches(PASSWORD_PATTERN)) {
            throw new IllegalArgumentException("Contraseña inválida");
        }
        return new Password(plain);
    }

    public static Password fromHashed(String hashed) {
        return new Password(hashed);
    }

    public String value() {
        return value;
    }
}
