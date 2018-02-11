package com.twosquares.e_mandi.datamodels;

import java.io.Serializable;
import com.google.gson.annotations.SerializedName;

/**
 * Created by PRASHANT on 16-11-2016.
 */

public class RowItem implements Serializable {

    @SerializedName("title") private String title;
    @SerializedName("image_id") private String image_id;
    @SerializedName("price") private String price;
    @SerializedName("phoneNo") private String contact;
    @SerializedName("location") private String location;
    @SerializedName("description") private String description;
    private Boolean important;
    @SerializedName("ownerId") private String owner_id;
    @SerializedName("quantity") private String quantity;

    public RowItem(String items[]){
        this.title = items[0];
        this.image_id = items[1];
        this.price = items[2];
        this.contact = items[3];
        this.location = items[4];
        this.description = items[5];
        this.important = Boolean.valueOf(items[6]);
        this.owner_id = items[7];
        this.quantity = items[8];

    }

    public String getImage_id() {
        return image_id;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getContact() {
        return contact;
    }

    public String getLocation() {
        return location;
    }
    public String getDescription(){
        return description;
    }
    public String getQuantity(){ return quantity;}

    public boolean isImportant() {
        return important;
    }

    public void setImportant(boolean important) {
        this.important = important;
    }
}
