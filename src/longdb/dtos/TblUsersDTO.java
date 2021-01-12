/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package longdb.dtos;

import java.io.Serializable;

/**
 *
 * @author ACER
 */
public class TblUsersDTO implements Serializable {

    private String username;
    private String password;
    private String fullName;
    private boolean status;

    public TblUsersDTO() {
    }

    public TblUsersDTO(String username, String password, String fullName, boolean status) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}
