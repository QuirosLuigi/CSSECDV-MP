package Model;

/**
 *
 * @author Zoe Avila
 */
public class Session {
    private int id;
    private String username;
    private int role = 2;

    public Session(String username, int role){
        this.username = username;
        this.role = role;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
