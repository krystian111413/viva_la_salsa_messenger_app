package com.zerter.teamconnect.MenageGroup.googlegroups;

/**
 * Created by krystiankowalski on 12.01.2018.
 */

public class Item {
    public String name,id,phNo,phDisplayName,phType;
    public boolean isChecked =false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhNo() {
        return phNo;
    }

    public void setPhNo(String phNo) {
        this.phNo = phNo;
    }

    public String getPhDisplayName() {
        return phDisplayName;
    }

    public void setPhDisplayName(String phDisplayName) {
        this.phDisplayName = phDisplayName;
    }

    public String getPhType() {
        return phType;
    }

    public void setPhType(String phType) {
        this.phType = phType;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
