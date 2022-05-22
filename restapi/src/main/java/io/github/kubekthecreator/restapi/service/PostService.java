package io.github.kubekthecreator.restapi.service;


import io.github.kubekthecreator.restapi.model.Comment;
import io.github.kubekthecreator.restapi.model.Post;
import io.github.kubekthecreator.restapi.repository.CommentRepository;
import io.github.kubekthecreator.restapi.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.criterion.Order;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    private static final int PAGE_SIZE = 20;
    public List<Post> getPosts(int page, Sort.Direction sort){
        return postRepository.findAllPosts(PageRequest.of(page,PAGE_SIZE, Sort.by(sort, "id")));
    }


    @Cacheable(cacheNames = "SinglePost", key = "#id")
    public Post getSinglePost(long id) {
        return postRepository.findById(id)
                .orElseThrow();
    }

    @Cacheable(cacheNames = "PostsWithComments")
    public List<Post> getPostsWithComments(int page, Sort.Direction sort){
        List<Post> allPosts;
        allPosts = postRepository.findAllPosts(
                PageRequest.of(page,PAGE_SIZE, Sort.by(sort, "id")));
        List<Long> ids = allPosts.stream()
                .map(post -> post.getId())
                .collect(Collectors.toList());
        List<Comment> comments = commentRepository.findAllByPostIdIn(ids);
        allPosts.forEach(post -> post.setComment(extractComment(comments, post.getId())));
        return allPosts;
    }

    private List<Comment> extractComment (List<Comment> comments, final long id) {
        return comments.stream()
                .filter(comment -> comment.getPostId() == id)
                .collect(Collectors.toList());
    }

    public Post addPost(final Post post) {
        return postRepository.save(post);
    }

    @Transactional
    @CachePut(cacheNames = "SinglePost", key = "#result.id")
    public Post editPost(final Post post) {
        Post postEdited = postRepository.findById(post.getId()).orElseThrow();
        postEdited.setTitle(post.getTitle());
        postEdited.setContent(post.getContent());
        return postEdited;
    }

    @CacheEvict(cacheNames = "SinglePost")
    public void deletePost(long id) {
        postRepository.deleteById(id);
    }
}
