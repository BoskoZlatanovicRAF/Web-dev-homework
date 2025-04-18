package rs.raf.demo.resources;


import rs.raf.demo.entities.Comment;
import rs.raf.demo.entities.Post;
import rs.raf.demo.services.PostService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/posts")
public class PostResource {

    @Inject
    private PostService postService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response all() {
        return Response.ok(this.postService.allPosts()).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("id") Integer id) {
        return Response.ok(this.postService.findPost(id)).build();
    }

    @GET
    @Path("{id}/comments")
    @Produces(MediaType.APPLICATION_JSON)
    public Response comments(@PathParam("id") Integer id) {
        return Response.ok(this.postService.findPost(id).getComments()).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@Valid Post post) {
        return Response.ok(this.postService.addPost(post)).build();
    }

    @POST
    @Path("{id}/comment")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createComment(@PathParam("id") Integer id, @Valid Comment comment) {
        comment.setPostId(id);
        return Response.ok(this.postService.addComment(comment)).build();
    }



}
