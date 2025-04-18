package rs.raf.demo.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;

    @NotNull(message = "Title field is required")
    @NotEmpty(message = "Title field is required")
    private String username;

    @NotNull(message = "Password field is required")
    @NotEmpty(message = "Password field is required")
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
