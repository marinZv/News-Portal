package rs.raf.demo.resources;

import rs.raf.demo.entities.User;
import rs.raf.demo.requests.LoginRequest;
import rs.raf.demo.services.UserService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/users")
public class UserResource {

    @Inject
    private UserService userService;

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Valid LoginRequest loginRequest){
        Map<String, String> response = new HashMap<>();

        String jwt = this.userService.login(loginRequest.getEmail(), loginRequest.getPassword());
        System.out.println("prosao sam userService.login()");
        if(jwt == null){
            response.put("message", "These credentials do not match out records");
            return Response.status(422, "Unprocessable Entity").entity(response).build();
        }

        response.put("jwt", jwt);

        System.out.println("Jwt: " + jwt);

        return Response.ok(response).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public User createUser(@Valid User user){
        System.out.println("Usao sam u addUser : UserResource");
        return this.userService.addUser(user);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> allUsers(){
        return this.userService.allUsers();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{email}")
    public User findUser(@PathParam("email") String email){
       return this.userService.findUser(email);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/status/{email}")
    public void changeUserActivity(@PathParam("email") String email){
        this.userService.changeUserActivity(email);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{email}")
    public User updateUser(@Valid User user, @PathParam("email") String email){
        return this.userService.updateUser(user, email);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{email}")
    public void deleteUser(@PathParam("email") String email){
        this.userService.deleteUser(email);
    }

}
