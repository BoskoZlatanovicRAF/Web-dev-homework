package rs.raf.demo.repositories.subject;

import rs.raf.demo.entities.Comment;
import rs.raf.demo.entities.Post;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class InMemoryPostRepository implements Repository {
    private static List<Post> posts = new CopyOnWriteArrayList<>();

    public InMemoryPostRepository() {
        Date date = new Date();
        String loremIpsum1 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus a gravida orci. Proin elementum pretium euismod. Nam volutpat vehicula tortor. Quisque pulvinar, enim vitae ornare congue, leo libero tincidunt justo, eget sodales odio ante at mauris. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam fermentum eleifend interdum. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Phasellus eget scelerisque dolor. Nam a augue elit. Donec in augue ligula. Quisque in gravida sapien. Interdum et malesuada fames ac ante ipsum primis in faucibus. Sed volutpat eros vitae quam tempor, sed scelerisque neque pulvinar. Mauris a eleifend massa";
        String loremIpsum2 = "Sed facilisis convallis ligula, ut lacinia ex. Sed mattis dolor nec est tristique sodales. Aenean interdum dolor nibh, sed commodo felis scelerisque at. Curabitur vel tortor placerat leo gravida convallis eget maximus mi. Nunc sit amet consequat ligula. Vestibulum interdum erat mi, eget aliquam odio fermentum hendrerit. Curabitur eget urna pulvinar, rhoncus eros eu, vulputate metus. In eget nulla sapien. Donec pharetra congue massa semper sollicitudin. Nam condimentum congue purus, vel lobortis tortor.";
        String loremIpsum3 = "Vestibulum nec neque egestas, congue lectus id, malesuada lectus. Maecenas elementum semper purus ac pulvinar. Vestibulum a sapien iaculis, imperdiet elit quis, interdum turpis. Proin sit amet nisl vel lorem consequat volutpat et ac felis. Praesent blandit dui orci, in egestas nisl tempor at. Morbi faucibus purus purus, ut pharetra diam elementum non. Phasellus cursus vel neque a imperdiet. Proin lacinia mauris eget ligula pulvinar tincidunt. Vestibulum lacinia libero quam, eget dictum ante pharetra eget.";
        Comment comment1 = new Comment(0, "Author 1", "Comment 1", 0);
        posts.add(new Post(0, "Author 1", "Title 1", loremIpsum1, date, new ArrayList<>()));
        posts.add(new Post(1, "Author 2", "Title 2", loremIpsum2, date, new ArrayList<>()));
        posts.add(new Post(2, "Author 3", "Title 3", loremIpsum3, date, new ArrayList<>()));

        posts.get(0).getComments().add(comment1);
    }


    @Override
    public Post addPost(Post post) {
        Integer id = posts.size();
        Random random = new Random();
        int daysToSubtract = random.nextInt(11);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -daysToSubtract);
        Date date = calendar.getTime();
        post.setId(id);
        post.setDate(date);
        posts.add(Math.toIntExact(id), post);

        return post;
    }

    @Override
    public List<Post> allPosts() {
        return new ArrayList<>(posts);
    }

    @Override
    public Comment addComment(Comment comment) {
        Integer id = posts.size();
        comment.setId(id);
        Post post = posts.get(comment.getPostId());
        post.getComments().add(comment);

        return comment;
    }

    @Override
    public Post findPost(Integer id) {
        return posts.get(id);
    }
}
