package com.locanthach.sharefood.dao;

import com.locanthach.sharefood.model.Like;
import com.locanthach.sharefood.model.Post;
import com.locanthach.sharefood.model.PostEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by An Lee on 7/30/2017.
 */

public class PostDAO {
    public void storePosts(List<Post> posts) {
        final List<PostEntity> entities = new ArrayList<>();
        for (Post post : posts) {
            entities.add(new PostEntity(
                    post.getId(),
                    post.getUid(),
                    post.getAuthor(),
                    post.getContent(),
                    post.getPhotoUrl(),
                    post.getLocation(),
                    post.getTime(),
                    post.getLikeCount(),
                    post.getStatus(),
                    post.getViewCount(),
                    post.getLikes()));
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
            Map<String, Boolean> likes = new HashMap<>();
            List<Like> likeList = entity.likes;
            for (int i = 0; i < likeList.size(); i++) {
                likes.put(likeList.get(i).key, Boolean.parseBoolean(likeList.get(i).value));
            }
            posts.add(new Post(
                    entity.id,
                    entity.uid,
                    entity.author,
                    entity.content,
                    entity.photoUrl,
                    entity.location,
                    entity.time,
                    entity.likeCount,
                    entity.status,
                    entity.viewCount,
                    likes));
        }
        return posts;
    }
}
