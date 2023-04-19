package model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
/**
 * @author Ayush Patel
 * @author Kirtan Patel
 *
 * this class helps undergo photo methods
 */
public class Photo implements Serializable
{
    /**
     * dat file storage aid
     */
    private static final long serialVersionUID = 1l;
    public static final String destinationDir = "dat";
    public static final String fileDesName = "nonAdminUsers.dat";
    /**
     * the photo name
     */
    public String photoName;
    /**
     * the picture in File format
     */
    public File picture;
    /**
     * list of tags in the form of an arraylist
     */
    public ArrayList<Tag> listOfTags;
    /**
     * the photo caption
     */
    public String caption;
    /**
     * calendar instance variable
     */
    public Calendar calendar;
    /**
     * date instance variable
     */
    public Date date;
    /**
     * boolean to check if the picture is one of the stock ones
     */
    public boolean isStockPic = false;

    /**
     * Constructor
     * @param picture the picture file
     * @param pictureName the picture name
     */
    public Photo(File picture, String pictureName)
    {
        this.photoName = pictureName;
        if(picture != null)
        {
            this.picture = new File(pictureName);
        }
        else
        {
            this.picture = picture;
        }
        this.listOfTags = new ArrayList<Tag>();
        calendar = new GregorianCalendar();
        calendar.set(Calendar.MILLISECOND, 0);
        this.date = calendar.getTime();
    }
    /**
     *
     * @return a File of the picture
     *
     * this method helps get the picture
     */
    public File getPicture()
    {
        return this.picture;
    }
    /**
     *
     * @return the date of the picture
     *
     * this method helps get the date of the picture
     */
    public Date getDate()
    {
        return date;
    }
    /**
     *
     * @return the caption of picture in the form of a string
     */
    public String getCaption()
    {
        return caption;
    }
    /**
     *
     * @param caption the caption to be set
     *
     * this method helps to set caption
     */
    public void setCaption(String caption)
    {
        this.caption = caption;
    }
    /**
     *
     * @param tag_name the tag name
     * @param tag_value the tag value
     *
     * this method helps in adding a tag
     */
    public void addTag(String tag_name, String tag_value)
    {
        listOfTags.add(new Tag(tag_name, tag_value));
    }
    /**
     *
     * @param tag_name the tag name
     * @param tag_value the tag value
     *
     * this method helps in deleting a tag
     */
    public void deleteTag(String tag_name, String tag_value)
    {
        for(int i = 0; i < listOfTags.size(); i++)
        {
            Tag curr = listOfTags.get(i);
            if(curr.tag_name.toLowerCase().equals(tag_name.toLowerCase()) && curr.tag_value.toLowerCase().equals(tag_value.toLowerCase()))
            {
                listOfTags.remove(i);
            }
        }
    }
    /**
     *
     * @param tag_name the tag name
     * @param tag_value the tag value
     * @return a boolean true if tag exists, false otherwise
     *
     * this method checks if tag exists
     */
    public boolean tagsOfPhotosExists(String tag_name, String tag_value)
    {
        for(int i = 0; i < listOfTags.size(); i++)
        {
            Tag curr = listOfTags.get(i);
            if(curr.tag_name.toLowerCase().equals(tag_name.toLowerCase()) && curr.tag_value.toLowerCase().equals(tag_value.toLowerCase()))
            {
                return true;
            }
        }

        return false;
    }
    /**
     *
     * @return an arraylist of Tags
     *
     * this method helps in getting the list of tags
     */
    public ArrayList<Tag> getListOfTags()
    {
        return listOfTags;
    }
    /**
     *
     * @return a string of picture name
     *
     * this method helps in getting the picture name
     */
    public String getPicName()
    {
        return photoName;
    }
    /**
     *
     * @return a string to print the picture name
     *
     * this method helps to print the string in proper english format
     */
    public String toString()
    {
        return getPicName();
    }
    /**
     *
     * @param p
     * @throws IOException
     *
     * this method helps to save photo data
     */
    public static void saveToDat(Photo p) throws IOException
    {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(destinationDir + File.separator + fileDesName));
        oos.writeObject(p);
        oos.close();
    }
}
