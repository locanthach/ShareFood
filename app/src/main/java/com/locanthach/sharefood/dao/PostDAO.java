package com.locanthach.sharefood.dao;

import com.locanthach.sharefood.entity.PostEntity;
import com.locanthach.sharefood.model.Post;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by An Lee on 7/16/2017.
 */

public class PostDAO {
    public void storeDAO(List<Post> posts) {
        final List<PostEntity> entities = new ArrayList<>();
        for (Post post : posts) {
            entities.add(new PostEntity(
                    post.getId(),
                    post.getTitle(),
                    post.getLocation(),
                    post.getTimeStamp(),
                    post.getPhotos(),
                    post.getComments(),
                    post.getLikeCount(),
                    post.isLiked(),
                    post.getUser()));
        }
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> realm1.copyToRealm(entities));
    }

    public void clearAll() {
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<PostEntity> results = realm.where(PostEntity.class).findAll();
        realm.executeTransaction(realm1 -> results.deleteAllFromRealm());
    }

    public List<Post> getAll() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<PostEntity> entities = realm.where(PostEntity.class)
                .findAll();
        List<Post> posts = new ArrayList<>();
        for (PostEntity entity : entities) {
            posts.add(new Post(
                    entity.id,
                    entity.title,
                    entity.location,
                    entity.timeStamp,
                    entity.photos,
                    entity.comments,
                    entity.likeCount,
                    entity.liked,
                    entity.user));
        }
        return posts;
    }
}
