package software.kuukkel.fi.recipics.Objects;

import android.widget.Button;

/**
 * Created by namiskuukkel on 29.1.2017.
 */

public class ButtonAndFilepath {


    private Button delete;
    private String filePath;

    public ButtonAndFilepath(Button delete, String filePath) {
        this.delete = delete;
        this.filePath = filePath;
    }

    public Button getDelete() {
        return delete;
    }

    public void setDelete(Button delete) {
        this.delete = delete;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}
