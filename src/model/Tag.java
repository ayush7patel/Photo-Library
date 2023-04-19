package model;

import java.io.Serializable;

/**
 * @author Ayush Patel
 * @author Kirtan Patel
 *
 * this class helps to deal with tag method
 */
public class Tag implements Serializable
{
    /**
     * the tag name and value
     */
    public String tag_name;
    public String tag_value;
    /**
     * Constructor
     * @param tag_name the tag name
     * @param tag_value the tag value
     *
     * this method helps make a tag
     */
    public Tag (String tag_name, String tag_value)
    {
        this.tag_name = tag_name;
        this.tag_value = tag_value;
    }
    /**
     *
     * @param o object o
     * @return true if the tags are equal to object o, false otherwise
     *
     * this method helps to check is tags are equal to each other
     */
    public boolean equals (Object o)
    {
        if(o == null || !(o instanceof Tag))
        {
            return false;
        }
        Tag tag = (Tag) o;
        return tag.tag_name.equals(this.tag_name) && tag.tag_value.equals(this.tag_value);
    }
}
