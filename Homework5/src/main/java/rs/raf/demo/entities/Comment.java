package rs.raf.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Comment {
    private Integer id;

    @NotNull(message = "Comment content is required")
    @NotEmpty(message = "Comment content cannot be empty")
    private String content;

    @NotNull(message = "Comment author is required")
    @NotEmpty(message = "Comment author cannot be empty")
    private String author;

    // Reference to the post it belongs to
    private Integer postId;
}
