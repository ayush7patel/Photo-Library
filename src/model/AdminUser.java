package model;

import application.Photos;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

/**
 * @author Ayush Patel
 * @author Kirtan Patel
 *
 * this class helps admin users undergo tasks only they have access to
 */
public class AdminUser implements Serializable
{
    /**
     * these helps with dat storage
     */
    private static final long serialVersionUID = 1L;
    public static final String directoryDes = "dat";
    public static final String fileDesName = "nonAdminUsers.dat";
    /**
     * arraylist for non-admin users
     */
    public ArrayList<NonAdminUser> users;
    /**
     * the current non-admin user that is logged in
     */
    public NonAdminUser curr;
    /**
     * boolean varibale to determine is the non-admin user is logged in or not
     */
    public boolean isLogged;
    /**
     * Constructor
     */
    public AdminUser()
    {
        users = new ArrayList<NonAdminUser>();
        users.add(new NonAdminUser("admin"));
        this.curr = null;
        this.isLogged = false;
    }
    /**
     *
     * @param username to store
     *
     * this method adds username in admin page
     */
    public void addUser(String username)
    {
        users.add(new NonAdminUser(username));
    }
    /**
     *
     * @param index to find where to delete user
     *
     * this method deletes a non-admin user from admin page given an index
     */
    public void deleteUserByIndex(int index)
    {
        users.remove(index);
    }
    /**
     *
     * @param username we are lookin up
     * @return true if username exists in admin page, false otherwise
     */
    public boolean doesUserExist(String username)
    {
        for(NonAdminUser user : users)
        {
            if(user.getUserName().equals(username))
            {
                return true;
            }
        }

        return false;
    }
    /**
     *
     * @return an arraylist of non-admin users
     */
    public ArrayList<NonAdminUser> getNonAdminUsers()
    {
        return users;
    }
    /**
     *
     * @param username for search adn return
     * @return a non-admin user
     */
    public NonAdminUser getNonAdminUserByStringInput(String username)
    {
        for(NonAdminUser user : users)
        {
            if(user.getUserName().equals(username))
            {
                return user;
            }
        }

        return null;
    }
    /**
     *
     * @return the current non-admin user
     */
    public NonAdminUser getCurrentUser()
    {
        return curr;
    }
    /**
     *
     * @param user we will be checking if exists in admin page
     * @return true if so, false otherwise
     */
    public boolean checkUserExistsInAdminSubsystem(String user)
    {
        int index = 0;
        for(int i = 0 ; i < users.size(); i++)
        {
            if(users.get(i).getUserName().equals(user))
            {
                index = i;
            }
        }

        if(index == -1)
        {
            return false;
        }

        this.setCurrentUser(users.get(index));
        this.isLogged = true;
        return true;
    }

    /**
     *
     * @return the index of the user we need to undergo a task with said user
     */
    public int getIndexOfUser()
    {
        int index = 0;
        for(NonAdminUser nau : users)
        {
            if(nau.getUserName().equals(Photos.driver.getCurrentUser().getUserName()))
            {
                return index;
            }
            index++;
        }

        return -1;
    }
    /**
     *
     * @param curr setting the current non-admin user
     */
    public void setCurrentUser(NonAdminUser curr)
    {
        this.curr = curr;
    }
    /**
     *
     * @param a
     * @throws IOException
     *
     * this method saves to dat file
     */
    public static void saveToDat(AdminUser a) throws IOException
    {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(directoryDes + File.separator + fileDesName));
        oos.writeObject(a);
        oos.close();
    }
}
