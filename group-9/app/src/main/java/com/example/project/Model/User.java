/** author: lice Liu and Nicole Ni
 * date: 2019.10.30
 * This class used to display the user information
 */

package com.example.project.Model;

public class User {
    private String id;
    private String email;
    private int type;

    public User() {
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", type=" + type +
                '}';
    }
}
