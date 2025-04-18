package rs.raf.demo.repositories.post;


import rs.raf.demo.entities.Comment;
import rs.raf.demo.entities.Post;

import java.util.List;

public interface Repository {
    public Post addPost(Post post);
    public List<Post> allPosts();
    public Comment addComment(Comment comment);
    public Post findPost(Integer id);

    List<Comment> findCommentsByPostId(Integer postId);
}
