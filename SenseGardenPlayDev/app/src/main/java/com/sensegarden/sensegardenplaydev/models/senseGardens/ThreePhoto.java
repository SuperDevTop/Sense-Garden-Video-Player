package com.sensegarden.sensegardenplaydev.models.senseGardens;

import com.sensegarden.sensegardenplaydev.models.user.PhotoVideo;

import java.util.ArrayList;

public class ThreePhoto {
    private PhotoVideo photo1;
    private PhotoVideo photo2;
    private PhotoVideo photo3;

    public ThreePhoto(PhotoVideo photo1, PhotoVideo photo2, PhotoVideo photo3) {
        this.photo1 = photo1;
        this.photo2 = photo2;
        this.photo3 = photo3;
    }

    public ArrayList<PhotoVideo> getPhotos() {
        ArrayList<PhotoVideo> photos = new ArrayList<>();
        photos.add(photo1);
        photos.add(photo2);
        photos.add(photo3);

        return photos;
    }

    public PhotoVideo getPhoto1() {
        return photo1;
    }

    public void setPhoto1(PhotoVideo photo1) {
        this.photo1 = photo1;
    }

    public PhotoVideo getPhoto2() {
        return photo2;
    }

    public void setPhoto2(PhotoVideo photo2) {
        this.photo2 = photo2;
    }

    public PhotoVideo getPhoto3() {
        return photo3;
    }

    public void setPhoto3(PhotoVideo photo3) {
        this.photo3 = photo3;
    }
}
