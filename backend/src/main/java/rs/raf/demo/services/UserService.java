package rs.raf.demo.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.codec.digest.DigestUtils;
import rs.raf.demo.entities.User;
import rs.raf.demo.repositories.user.UserRepository;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

public class UserService {

    @Inject
    UserRepository userRepository;

    public String login(String email, String password){
        String hashedPassword = DigestUtils.sha256Hex(password);

        System.out.println("Usao sam u userservice");
        User user = this.userRepository.findUser(email);

        if(user == null || !user.getPassword().equals(hashedPassword)){
            return null;
        }

        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + 24*60*60*1000);

        Algorithm algorithm = Algorithm.HMAC256("secret");

        return JWT.create()
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .withSubject(email)
                .withClaim("role", user.getRole())
                .withClaim("status", user.getStatus())
                .sign(algorithm);
    }

    public boolean isAuthorized(String token){
        Algorithm algorithm = Algorithm.HMAC256("secret");
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token);

        String email = jwt.getSubject();

        User user = this.userRepository.findUser(email);

        if(user == null){
            return false;
        }

        return true;
    }

    public User addUser(User user){
        return this.userRepository.addUser(user);
    }

    public List<User> allUsers(){
        System.out.println("usao sam u allUsers u userservicu");
        return this.userRepository.allUsers();
    }

    public User findUser(String email){
        return this.userRepository.findUser(email);
    }

    public void changeUserActivity(String email){
        this.userRepository.changeUserActivity(email);
    }

    public void deleteUser(String email){
        this.userRepository.deleteUser(email);
    }

    public User updateUser(User user, String email){
        return this.userRepository.updateUser(user, email);
    }
}
