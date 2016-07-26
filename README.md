# [Realm을 이용한 안드로이드 메모장 앱]

### 1. install

```build.gradle(project)
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath "io.realm:realm-gradle-plugin:1.1.0"
    }
}
```

```build.gradle(app)
apply plugin: 'realm-android'
```

### 2. Proguard
필요없음.

### 3. Memo.class : RealmObject

```
package com.tistory.massivcode.realmmemoapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by massivcode on 2016. 7. 25..
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

```

### 4. CrudWrapper.class : Realm을 이용한 CRUD HelperClass

```
package com.tistory.massivcode.realmmemoapp;

import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by massivcode on 2016. 7. 25..
 */
public enum CrudWrapper {
    Instance;

    public ArrayList<Memo> selectAllMemos(Realm realm) {
        ArrayList<Memo> result = new ArrayList<>();

        RealmResults<Memo> realmResults = realm.where(Memo.class).findAllSorted("createDate", Sort.ASCENDING);

        int size = realmResults.size();

        // Realm 의 쿼리 결과인 RealmResults 는 쿼리 결과가 없더라도 null 이 리턴되는 대신, size 가 0으로 나온다.
        if (size != 0) {
            for (int i = 0; i < size; i++) {
                result.add(realmResults.get(i));
            }
        }

        return result;
    }

    public void insertMemo(Realm realm, final Memo memo) {
        int lastId = 0;
        try {
            lastId = realm.where(Memo.class).max("id").intValue() + 1;
        } catch (NullPointerException e) {
            System.out.println(e.toString());
        } finally {
            memo.setId(lastId);
        }

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(memo);
            }
        });
    }

    public void updateMemo(Realm realm, final Memo memo) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                System.out.println(memo);
                int id = memo.getId();
                Memo targetMemo = realm.where(Memo.class).equalTo("id", id).findFirst();
                System.out.println(targetMemo);
                targetMemo.setTitle(memo.getTitle());
                targetMemo.setContents(memo.getContents());
                targetMemo.setUpdateDate(new Date(System.currentTimeMillis()));
            }
        });
    }

    public void deleteMemo(Realm realm, final Memo memo) {
        final RealmResults<Memo> results = realm.where(Memo.class).equalTo("id", memo.getId()).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteAllFromRealm();
            }
        });
    }
}

```


