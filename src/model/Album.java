package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Ayush Patel
 * @author Kirtan Patel
 *
 * this class helps implement Album features
 */
public class Album implements Serializable
{
    /**
     * dat file storage aid
     */
    private static final long serialVersionUID = 1l;
    public static final String destinationDir = "dat";
    public static final String fileDesName = "nonAdminUsers.dat";
    /**
     * the album name as a string
     */
    public String albumName;
    /**
     * list of photos for an album in arraylist format
     */
    public ArrayList<Photo> listOfPhotos;
    /**
     * int counter for number of photos in an album
     */
    public int countOfPhotos = 0;
    /**
     * the current photo in an album
     */
    public Photo curr;
    /**
     * Constructor
     * @param albumName the album name
     */
    public Album(String albumName)
    {
        this.albumName = albumName;
        listOfPhotos = new ArrayList<Photo>();
    }
    /**
     *
     * @return a string of the album name
     *
     * a get method to get album name
     */
    public String getAlbumName()
    {
        return this.albumName;
    }
    /**
     *
     * @param albumName the album name to be re-named
     *
     * this method helps rename an album
     */
    public void renamedAlbumInput(String albumName)
    {
        this.albumName = albumName;
    }
    /**
     *
     * @param curr the current photo in the album
     *
     * this method helps set the current photo to param
     */
    public void setCurrUserPhoto(Photo curr)
    {
        this.curr = curr;
    }
    /**
     *
     * @param index the index for which we are using to delete the photo
     *
     * this method helps in deleting a photo from an album
     */
    public void delPhoto(int index)
    {
        listOfPhotos.remove(index);
        countOfPhotos--;
    }
    /**
     *
     * @param photo the photo to be added
     *
     * this method helps in adding a new photo to album
     */
    public void addNewUserPhoto(Photo photo)
    {
        listOfPhotos.add(photo);
        countOfPhotos++;
    }
    /**
     *
     * @return a string of the beginning date
     *
     * this method helps in getting the first date
     */
    public String getFirstDate()
    {
        SimpleDateFormat df = new SimpleDateFormat("E, M-d-y 'at' h:m:s a");
        Date d = null;
        String ds = "No Date";
        if(!listOfPhotos.isEmpty())
        {
            d = this.getPhotos().get(0).date;
            for(Photo photo: listOfPhotos)
            {
                if(photo.date.before(d))
                {
                    d = photo.date;
                }
            }

            ds = df.format(d);
        }

        return ds;
    }
    /**
     *
     * @return a string of the ending date
     *
     * this method helps in getting the last date
     */
    public String getLastDate()
    {
        SimpleDateFormat df = new SimpleDateFormat("E, M-d-y 'at' h:m:s a");
        Date d = null;
        String ds = "No Date";
        if(!listOfPhotos.isEmpty())
        {
            d = this.getPhotos().get(0).date;
            for(Photo photo: listOfPhotos)
            {
                if(photo.date.after(d))
                {
                    d = photo.date;
                }
            }

            ds = df.format(d);
        }

        return ds;
    }
    /**
     *
     * @return an arraylist of photos
     *
     * this method helps in getting photos
     */
    public ArrayList<Photo> getPhotos()
    {
        return listOfPhotos;
    }
    /**
     *
     * @return a photo of the curr non-admin user
     *
     * this method helps in getting the curr user photo
     */
    public Photo getCurrUserPhoto()
    {
        return curr;
    }
    /**
     *
     * @return a string
     *
     * to string method to print
     */
    public String toString()
    {
        return getAlbumName();
    }
    /**
     *
     * @param a
     * @throws IOException
     *
     * used to save data from albums to dat
     */
    public static void saveToDat(Album a) throws IOException
    {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(destinationDir + File.separator + fileDesName));
        oos.writeObject(a);
        oos.close();
    }
}
