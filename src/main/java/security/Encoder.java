package security;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Encoder {

    public static String encode(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }
}
