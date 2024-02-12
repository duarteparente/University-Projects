package Business.RacingManager.User;

public  class User {

    private String username;
    private String password;
    private String type;

    public User(String username, String password, String type){
        this.username = username;
        this.password = password;
        this.type = type;
    }

    public User (User u){
        this.username = u.getUsername();
        this.password = u.getPassword();
        this.type = u.getType();
    }

    //Getters
    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }

    public String getType(){
        return this.type;
    }

    //Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setType(String type){this.type = type;}



    @Override
    //Equals
    public boolean equals(Object o){
        if(this == o) return true;
        if((o == null) || (this.getClass() != o.getClass())) return false;
        User c = (User) o;
        return (this.username.equals(c.getUsername()) && this.password.equals(c.getPassword()));
    }

    ///clone
    @Override
    public User clone() {
        return new User(this);
    }
}
