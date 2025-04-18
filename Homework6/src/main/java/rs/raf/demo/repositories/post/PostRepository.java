package rs.raf.demo.repositories.post;

import rs.raf.demo.entities.Comment;
import rs.raf.demo.entities.Post;
import rs.raf.demo.repositories.MySqlAbstractRepository;

import javax.inject.Singleton;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostRepository extends MySqlAbstractRepository implements Repository {


    @Override
    public Post addPost(Post post) {
        String sql = "INSERT INTO posts (author_username, title, content) VALUES (?, ?, ?)";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, post.getAuthor_username());
            statement.setString(2, post.getTitle());
            statement.setString(3, post.getContent());
            statement.executeUpdate();

            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                post.setId(resultSet.getInt(1));
            }

            return post;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving post", e);
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(statement);
            this.closeConnection(connection);
        }
    }

    @Override
    public List<Post> allPosts() {
        String sql = "SELECT posts.id, posts.title, posts.content, posts.created_at, users.username AS author_username " +
                "FROM posts JOIN users ON posts.author_username = users.username";
        List<Post> posts = new ArrayList<>();

        try (Connection connection = this.newConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Post post = new Post();
                post.setId(resultSet.getInt("id"));
                post.setAuthor_username(resultSet.getString("author_username"));
                post.setTitle(resultSet.getString("title"));
                post.setContent(resultSet.getString("content"));
                post.setDate(resultSet.getTimestamp("created_at"));
                posts.add(post);
            }

            return posts;
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching posts", e);
        }
    }


    @Override
    public Comment addComment(Comment comment) {
        String sql = "INSERT INTO comments (post_id, author_username, content) VALUES (?, ?, ?)";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, comment.getPost_id());
            statement.setString(2, comment.getAuthor_username());
            statement.setString(3, comment.getContent());
            statement.executeUpdate();

            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                comment.setId(resultSet.getInt(1));
            }

            return comment;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving comment", e);
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(statement);
            this.closeConnection(connection);
        }
    }

    @Override
    public Post findPost(Integer id) {
        String sql = "SELECT * FROM posts WHERE id = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();
            statement = connection.prepareStatement(sql);

            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Post post = new Post();
                post.setId(resultSet.getInt("id"));
                post.setAuthor_username(resultSet.getString("author_username"));
                post.setTitle(resultSet.getString("title"));
                post.setContent(resultSet.getString("content"));
                post.setDate(resultSet.getTimestamp("created_at"));
                return post;
            }

            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding post by id", e);
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(statement);
            this.closeConnection(connection);
        }
    }

    @Override
    public List<Comment> findCommentsByPostId(Integer postId) {
        String sql = "SELECT * FROM comments WHERE post_id = ?";
        List<Comment> comments = new ArrayList<>();

        try (Connection connection = this.newConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, postId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Comment comment = new Comment();
                    comment.setId(resultSet.getInt("id"));
                    comment.setPost_id(resultSet.getInt("post_id"));
                    comment.setAuthor_username(resultSet.getString("author_username"));
                    comment.setContent(resultSet.getString("content"));
                    comments.add(comment);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding comments by post ID", e);
        }

        return comments;    }
}
