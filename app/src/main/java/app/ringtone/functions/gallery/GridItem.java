package app.ringtone.functions.gallery;

import java.io.File;

/**
 * Created by Javi on 14/12/2015.
 */
public class GridItem {
    private String image;
    private String title;
    private File file;
    public GridItem() {
        super();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public File getFile(){return file;}

    public void setFile(File newfile){this.file = newfile;}
}
