package io.github.kubekthecreator.restapi.controller;


import io.github.kubekthecreator.restapi.controller.dto.PostDto;
import io.github.kubekthecreator.restapi.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import io.github.kubekthecreator.restapi.model.Post;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class PostController {


    private final PostService postService;

    @GetMapping("/posts")
    public List<PostDto> getPosts(@RequestParam(required = false) Integer page, Sort.Direction sort,
                                  @AuthenticationPrincipal UsernamePasswordAuthenticationToken user){
        int pageNumber = page!= null && page >= 0 ? page : 0;
        Sort.Direction sortDirection = sort != null ? sort : Sort.Direction.ASC;
        return PostDtoMapper.mapToPostDtos (postService.getPosts(pageNumber, sortDirection));
    }

    @GetMapping("/posts/comments")
    public List<Post> getPostsWithComment(@RequestParam(required = false) Integer page, Sort.Direction sort){
        int pageNumber = page!= null && page >= 0 ? page : 0;
        Sort.Direction sortDirection = sort != null ? sort : Sort.Direction.ASC;
        return postService.getPostsWithComments(pageNumber, sortDirection);
    }


    @GetMapping("/posts/{id}")
    public Post getSinglePost(@PathVariable long id){
        return postService.getSinglePost(id);
    }


    @PostMapping("/posts")
    public Post addPost(@RequestBody Post post){
        return postService.addPost(post);
    }

    @PutMapping("/posts")
    public Post editPost(@RequestBody Post post){
        return postService.editPost(post);
    }

    @DeleteMapping("/posts/{id}")
    public void deletePost(@PathVariable long id){
        postService.deletePost(id);
    }
}
