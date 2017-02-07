package software.kuukkel.fi.recipics.Objects;

import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by namiskuukkel on 29.1.2017.
 */

public class ButtonAndFilepath {


    private ImageButton delete;
    private String filePath;

    public ButtonAndFilepath(ImageButton delete, String filePath) {
        this.delete = delete;
        this.filePath = filePath;
    }

    public ImageButton getDelete() {
        return delete;
    }

    public void setDelete(ImageButton delete) {
        this.delete = delete;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}
