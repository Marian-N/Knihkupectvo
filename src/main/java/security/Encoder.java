package security;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Encoder {

    /**
     * Encode password with BCrypt from spring security crypto
     * @param password
     * @return
     */
    public static String encode(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }
}
