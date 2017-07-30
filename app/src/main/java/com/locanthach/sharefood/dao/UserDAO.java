package com.locanthach.sharefood.dao;

import com.locanthach.sharefood.model.User;
import com.locanthach.sharefood.model.UserEntity;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by An Lee on 7/30/2017.
 */

public class UserDAO {
    public void storeUsers(List<User> users) {
        final List<UserEntity> entities = new ArrayList<>();
        for (User user : users) {
            entities.add(new UserEntity(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getProfileImageUrl()));
        }
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> realm1.copyToRealm(entities));
    }

    public void clearAll() {
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<UserEntity> results = realm.where(UserEntity.class).findAll();
        realm.executeTransaction(realm1 -> results.deleteAllFromRealm());
    }

    public List<User> getAll() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<UserEntity> entities = realm.where(UserEntity.class)
                .findAll();
        List<User> users = new ArrayList<>();
        for (UserEntity entity : entities) {
            users.add(new User(
                    entity.id,
                    entity.name,
                    entity.email,
                    entity.profileImageUrl));
        }
        return users;
    }
}
