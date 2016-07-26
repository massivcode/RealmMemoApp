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
