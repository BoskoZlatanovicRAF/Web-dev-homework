document.addEventListener('DOMContentLoaded', function() {
    const loginSection = document.getElementById('login-section');
    const homepageDiv = document.getElementById('homepage');
    const postFormDiv = document.getElementById('post-form');
    const fullPostDiv = document.getElementById('full-post');
    const loginForm = document.getElementById('login-form');
    const loginError = document.getElementById('login-error');

    loginForm.addEventListener('submit', async function(event) {
        event.preventDefault();

        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;

        try {
            const response = await fetch('/api/users/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ username, password })
            });

            if (!response.ok) {
                throw new Error('Login failed');
            }

            const token = await response.text();
            console.log('User logged in, received token:', token);

            // Store the token for future requests
            localStorage.setItem('username', username);
            localStorage.setItem('token', token);

            // Hide login section and show main content
            loginSection.style.display = 'none';
            homepageDiv.style.display = '';
            await loadPosts();  // Load posts after successful login

        } catch (error) {
            loginError.textContent = 'Invalid username or password';
            console.error('Error:', error);
        }
    });

    async function loadPosts() {
        try {
            const token = localStorage.getItem('token');
            const response = await fetch('/api/posts', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                }
            });

            if (!response.ok) {
                throw new Error('Failed to load posts');
            }

            const posts = await response.json();
            const postsContainer = document.getElementById('posts-container');
            postsContainer.innerHTML = '';  // Clear existing posts

            for (const post of posts) {
                displayPost(postsContainer, post);
            }

            // Check if the button already exists
            let newPostButton = document.getElementById('new-post-button');
            if (!newPostButton) {
                newPostButton = document.createElement('button');
                newPostButton.id = 'new-post-button';
                newPostButton.textContent = 'New Post';
                newPostButton.className = 'btn btn-primary';
                newPostButton.style.marginTop = '10px';
                postsContainer.after(newPostButton);

                newPostButton.addEventListener('click', showForm);
            }

        } catch (error) {
            console.error('Error:', error);
        }
    }

    function displayPost(postsContainer, post) {
        const listItem = document.createElement('li');
        listItem.className = 'list-group-item';

        const author = document.createElement('p');
        author.textContent = `Author: ${post.author_username}`;
        listItem.appendChild(author);

        const title = document.createElement('h5');
        title.textContent = `Title: ${post.title}`;
        listItem.appendChild(title);

        const content = document.createElement('p');
        if (post.content.length > 100) {
            content.textContent = `Content: ${post.content.substring(0, 100)}...`;
            const moreLink = document.createElement('a');
            moreLink.textContent = 'More';
            moreLink.href = '#';
            moreLink.style.color = 'blue';
            moreLink.style.textDecoration = 'underline';
            moreLink.onclick = function(event) {
                event.preventDefault();
                showFullPost(post);
            };
            content.appendChild(moreLink);
        } else {
            content.textContent = `Content: ${post.content}`;
        }
        listItem.appendChild(content);

        const currentDate = new Date();
        const postDate = new Date(post.date);
        const timeDiff = Math.abs(currentDate.getTime() - postDate.getTime());
        const diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24));

        const date = document.createElement('p');
        if (diffDays === 0) {
            date.textContent = 'Date: Today';
        } else if (diffDays === 1) {
            date.textContent = 'Date: 1 day ago';
        } else {
            date.textContent = `Date: ${diffDays} days ago`;
        }
        listItem.appendChild(date);

        postsContainer.appendChild(listItem);
    }

    function showForm() {
        const authorInput = document.getElementById('author');
        const username = localStorage.getItem('username');

        if (username) {
            authorInput.value = username;
        }

        postFormDiv.style.display = '';
        homepageDiv.style.display = 'none';
        fullPostDiv.style.display = 'none';
    }

    function showHomepage() {
        postFormDiv.style.display = 'none';
        homepageDiv.style.display = '';
        fullPostDiv.style.display = 'none';
        loadPosts();  // Reload posts when showing homepage
    }

    async function showFullPost(post) {
        homepageDiv.style.display = 'none';
        postFormDiv.style.display = 'none';

        fullPostDiv.style.display = '';
        fullPostDiv.innerHTML = '';

        const title = document.createElement('h2');
        title.textContent = post.title;
        fullPostDiv.appendChild(title);

        const author = document.createElement('p');
        author.textContent = `Author: ${post.author_username}`;
        fullPostDiv.appendChild(author);

        const content = document.createElement('p');
        content.textContent = `Content: ${post.content}`;
        fullPostDiv.appendChild(content);

        const date = document.createElement('p');
        date.textContent = `Date: ${new Date(post.date).toDateString()}`;
        fullPostDiv.appendChild(date);

        const token = localStorage.getItem('token');
        const response = await fetch(`/api/posts/${post.id}/comments`, {
            method: 'GET',
            headers:{
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });
        const comments = await response.json();

        const commentsTitle = document.createElement('h3');
        commentsTitle.textContent = 'Comments';
        fullPostDiv.appendChild(commentsTitle);

        const commentsList = document.createElement('ul');
        commentsList.className = 'list-group';
        for (const comment of comments) {
            const listItem = document.createElement('li');
            listItem.className = 'list-group-item';

            const author = document.createElement('strong');
            author.textContent = comment.author_username;
            listItem.appendChild(author);

            const content = document.createElement('p');
            content.textContent = comment.content;
            listItem.appendChild(content);

            commentsList.appendChild(listItem);
        }
        fullPostDiv.appendChild(commentsList);

        const commentForm = document.createElement('form');
        commentForm.innerHTML = `
              <div class="mb-3">
                  <label for="comment-author" class="form-label">Author</label>
                  <input type="text" class="form-control" id="comment-author" value="${post.author_username}">
              </div>
              <div class="mb-3">
                  <label for="comment-content" class="form-label">Content</label>
                  <textarea class="form-control" id="comment-content" placeholder="Content"></textarea>
              </div>
              <button type="button" id="comment-submit" class="btn btn-primary">Comment</button>
            `;
        fullPostDiv.appendChild(commentForm);

        document.getElementById('comment-submit').addEventListener('click', function() {
            submitNewComment(post.id);
        });
    }

    window.submitNewComment = async function submitNewComment(postId) {
        const author = document.getElementById('comment-author').value;
        const content = document.getElementById('comment-content').value;

        const newComment = {
            post_id: postId,
            author_username: author,
            content: content
        };
        console.log(postId);
        const token = localStorage.getItem('token');
        try {
            const response = await fetch(`/api/posts/${postId}/comment`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(newComment)
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText);
            }

            showHomepage();

        } catch (error) {
            console.error('Error:', error);
        }
    }

    window.submitNewPost = async function submitNewPost() {
        const author = document.getElementById('author').value;
        const title = document.getElementById('title').value;
        const content = document.getElementById('content').value;

        const newPost = {
            author_username: author,
            title: title,
            content: content
        };

        const token = localStorage.getItem('token');
        try {
            const response = await fetch('/api/posts', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(newPost)
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText);
            }

            showHomepage();

        } catch (error) {
            console.error('Error:', error);
        }
    }

});