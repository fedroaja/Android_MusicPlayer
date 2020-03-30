package umn.ac.id.uts2020_mobile_cl_00000021661_fedro_musique;

import java.io.Serializable;

public class Data implements Serializable {

    private String currTitle ="";
    private String currArtist ="";
    private String currDir = "";
    public Data(String currTitle,String currArtist,String currDir){
        this.currTitle = currTitle;
        this.currArtist = currArtist;
        this.currDir = currDir;
    }

    public String getTitle(){
        return currTitle;
    }
    public String getCurrArtist(){
        return currArtist;
    }
    public String getCurrDir(){
        return currDir;
    }

    public void setTitle(String currTitle){
        this.currTitle = currTitle;
    }
    public void setArtist(String currArtist){
        this.currArtist = currArtist;
    }
    public void setDir(String currDir){
        this.currDir = currDir;
    }



}
