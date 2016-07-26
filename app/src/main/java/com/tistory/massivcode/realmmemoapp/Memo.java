package com.tistory.massivcode.realmmemoapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by massivcode on 2016. 7. 25..
 * <p/>
 * PrimaryKey : 기본 키
 * Required : Not Null
 */
public class Memo extends RealmObject implements Parcelable {
    @PrimaryKey
    private int id;
    @Required
    private String title;
    @Required
    private String contents;
    @Required
    private Date createDate;
    private Date updateDate;

    public Memo() {
    }

    public Memo(int id, String title, String contents, Date updateDate) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.updateDate = updateDate;
    }

    public Memo(String title, String contents, Date createDate) {
        this.title = title;
        this.contents = contents;
        this.createDate = createDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Memo{");
        sb.append("id=").append(id);
        sb.append(", title='").append(title).append('\'');
        sb.append(", contents='").append(contents).append('\'');
        sb.append(", createDate=").append(createDate);
        sb.append(", updateDate=").append(updateDate);
        sb.append('}');
        return sb.toString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.contents);
        dest.writeLong(this.createDate != null ? this.createDate.getTime() : -1);
        dest.writeLong(this.updateDate != null ? this.updateDate.getTime() : -1);
    }

    protected Memo(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.contents = in.readString();
        long tmpCreateDate = in.readLong();
        this.createDate = tmpCreateDate == -1 ? null : new Date(tmpCreateDate);
        long tmpUpdateDate = in.readLong();
        this.updateDate = tmpUpdateDate == -1 ? null : new Date(tmpUpdateDate);
    }

    public static final Creator<Memo> CREATOR = new Creator<Memo>() {
        @Override
        public Memo createFromParcel(Parcel source) {
            return new Memo(source);
        }

        @Override
        public Memo[] newArray(int size) {
            return new Memo[size];
        }
    };
}
