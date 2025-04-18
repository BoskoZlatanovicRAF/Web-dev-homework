package rs.raf.demo.services;


import rs.raf.demo.entities.Comment;
import rs.raf.demo.entities.Post;
import rs.raf.demo.repositories.post.Repository;

import javax.inject.Inject;
import java.util.List;

public class PostService {

    @Inject
    private Repository repository;


    public Post addPost(Post post) {
        return this.repository.addPost(post);
    }

    public List<Post> allPosts() {
        return this.repository.allPosts();
    }

    public Comment addComment(Comment comment) {
        return this.repository.addComment(comment);
    }

    public Post findPost(Integer id) {
        return this.repository.findPost(id);
    }

    public List<Comment> findCommentsByPostId(Integer postId) {
        return repository.findCommentsByPostId(postId);
    }
}
