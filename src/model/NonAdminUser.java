package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

/**
 * @author Ayush Patel
 * @author Kirtan Patel
 *
 * this class helps the non-admin users undergo tasks specific to them
 */
public class NonAdminUser implements Serializable
{
    /**
     * dat file saving
     */
    private static final long serialVersionUID = 1l;
    public static final String destinationDir = "dat";
    public static final String fileDesName = "nonAdminUsers.dat";
    /**
     * username of non-admin user
     */
    public String username;
    /**
     * non-admin users' albums
     */
    public ArrayList<Album> albums;
    /**
     * the curr album of the non-admin user
     */
    public Album currAlbum;
    /**
     * Constructor
     * @param username the username of the non-admin user
     */
    public NonAdminUser(String username)
    {
        this.username = username;
        albums = new ArrayList<Album>();
    }
    /**
     *
     * @param album the album to be added
     *
     * this class adds the album name to non-admin catalog
     */
    public void addUserAlbum(Album album)
    {
        albums.add(album);
    }
    /**
     *
     * @param index index of non-admin user to be deleted
     *
     * this method deleted non-admin user
     */
    public void deleteUserAlbum(int index)
    {
        albums.remove(index);
    }
    /**
     *
     * @return a string which is the non-admin username
     *
     * helps get username of the non-admin user
     */
    public String getUserName()
    {
        return username;
    }
    /**
     *
     * @param albumName the inputted album we are checking if exists in the non-admin users' catalog
     * @return true if so, false otherwise
     */
    public boolean checkIfAlbumExistsInNonAdminUsers(Album albumName)
    {
        for(Album a : albums)
        {
            if(a.getAlbumName().equals(albumName))
            {
                return true;
            }
        }

        return false;
    }
    /**
     *
     * @param source the from date
     * @param destination the to date
     * @return an arraylist of photos within the date range
     *
     * this method helps select photos from a certain date range
     */
    public ArrayList<Photo> getPhotosFromDateRange(LocalDate source, LocalDate destination)
    {
        ArrayList<Photo> range = new ArrayList<Photo>();
        Calendar beg = Calendar.getInstance();
        beg.set(source.getYear(), source.getMonthValue(), source.getDayOfMonth());
        Calendar end = Calendar.getInstance();
        end.set(destination.getYear(), destination.getMonthValue(), destination.getDayOfMonth());

        for(Album a : albums)
        {
            for(Photo p : a.getPhotos())
            {
                Date d = p.getDate();
                Calendar picDate = Calendar.getInstance();
                picDate.setTime(d);
                Calendar t = Calendar.getInstance();
                int y = picDate.get(Calendar.YEAR);
                int m = picDate.get(Calendar.MONTH);
                int dom = picDate.get(Calendar.DAY_OF_MONTH);
                t.set(y, m, dom);
                if(t.compareTo(beg) > 0 && t.compareTo(end) < 0 || t.equals(beg) || t.equals(end))
                {
                    range.add(p);
                }
            }
        }

        return range;
    }
    /**
     *
     * @param tagList list of tags for AND search
     * @return an arraylist of photos
     *
     * helps with AND search
     */
    public ArrayList<Photo> getConjunctiveTagPhotos(ArrayList<Tag> tagList)
    {
        ArrayList<Photo> pList = new ArrayList<Photo>();
        HashSet<Photo> hs = new HashSet<Photo>();
        for(Album a : albums)
        {
            for(Photo p : a.getPhotos())
            {
                if(p.getListOfTags().containsAll(tagList))
                {
                    hs.add(p);
                }
            }
        }

        pList.addAll(hs);
        return pList;
    }
    /**
     *
     * @param tagList list of tags for OR search
     * @return an arraylist of photos
     *
     * helps with OR search
     */
    public ArrayList<Photo> getDisjunctiveTagPhotos(ArrayList<Tag> tagList)
    {
        ArrayList<Photo> pList = new ArrayList<Photo>();
        HashSet<Photo> hs = new HashSet<Photo>();
        for(Tag t : tagList)
        {
            for(Album a : albums)
            {
                for(Photo p : a.getPhotos())
                {
                    if(p.tagsOfPhotosExists(t.tag_name, t.tag_value))
                    {
                        hs.add(p);
                    }
                }
            }
        }

        pList.addAll(hs);
        return pList;
    }
    /**
     *
     * @return the current album
     *
     * helps retrieve the current album
     */
    public Album getCurrAlbum()
    {
        return currAlbum;
    }
    /**
     *
     * @return am arraylist of non-admin user albums
     *
     * this method helps retrieve albums of the user
     */
    public ArrayList<Album> getUserAlbums()
    {
        return albums;
    }
    /**
     *
     * @param index index of the non-admin user album we want
     * @return the album
     *
     * this method helps retrieve the album given an index
     */
    public Album getUserAlbumGivenIndex(int index)
    {
        return albums.get(index);
    }
    /**
     *
     * @param currAlbum the current album we want to set
     *
     *  set method to set currAlbum
     */
    public void setCurrUserAlbum(Album currAlbum)
    {
        this.currAlbum = currAlbum;
    }
    /**
     *
     * @param n
     * @throws IOException
     *
     * this method helps save non-admin user information to dat file
     */
    public static void saveToDat(NonAdminUser n) throws IOException
    {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(destinationDir + File.separator + fileDesName));
        oos.writeObject(n);
        oos.close();
    }
}
