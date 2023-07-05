package my.groupId.quarkussocial.rest;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import my.groupId.quarkussocial.domain.model.Follower;
import my.groupId.quarkussocial.domain.model.Post;
import my.groupId.quarkussocial.domain.model.UserEntity;
import my.groupId.quarkussocial.domain.repository.FollowerRepository;
import my.groupId.quarkussocial.domain.repository.PostRepository;
import my.groupId.quarkussocial.domain.repository.UserRepository;
import my.groupId.quarkussocial.rest.dto.CreatePostRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestHTTPEndpoint(PostResources.class)
class PostResourcesTest {

    @Inject
    UserRepository userRepository;
    @Inject
    FollowerRepository followerRepository;

    @Inject
    PostRepository postRepository;
    Long userId;
    Long userNotFollowerId;
    Long userFollowerId;

    @BeforeEach
    @Transactional
    public void setUP() {

        //usuario padrao dos testes
        var user = new UserEntity();
        user.setAge(30);
        user.setName("Fulano");
        userRepository.persist(user);
        userId = user.getId();

        //criada a postagem para o usuario
        Post post = new Post();
        post.setText("Hello");
        post.setUser(user);
        postRepository.persist(post);

        //usuario nao seguidor
        var userNotFollower = new UserEntity();
        userNotFollower.setAge(30);
        userNotFollower.setName("Cicrano");
        userRepository.persist(userNotFollower);
        userNotFollowerId = userNotFollower.getId();

        //usuario seguidor
        var userFollower = new UserEntity();
        userFollower.setAge(30);
        userFollower.setName("Cicrano");
        userRepository.persist(userFollower);
        userFollowerId = userFollower.getId();

        Follower follower = new Follower();
        follower.setUser(user);
        follower.setFollower(userFollower);
        followerRepository.persist(follower);
    }
    @Test
    @DisplayName("should create a post for a user")
    public void createPostTest(){
        var postRequest = new CreatePostRequest();
        postRequest.setText("Some text");

        given()
                .contentType(ContentType.JSON)
                .body(postRequest)
                .pathParam("userId", userId)
                .when()
                .post()
                .then()
                .statusCode(201);
    }

    @Test
    @DisplayName("should return 404 when trying to make a post for an inexistent user")
    public void postForAnInexistentUserTest(){
        var postRequest = new CreatePostRequest();
        postRequest.setText("Some text");

        var inexistentUserId = 999;

        given()
                .contentType(ContentType.JSON)
                .body(postRequest)
                .pathParam("userId", inexistentUserId)
                .when()
                .post()
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("should return 404 when user does not exist")
    public void listPostUserNotFoundText() {

        var inexistentUserId = 999;
        given()
                .pathParam("userId", inexistentUserId)
                .when()
                .get()
                .then()
                .statusCode(404);

    }

    @Test
    @DisplayName("should return 404 when followerId header is not present")
    public void listPostFollowerHeaderNotSentText() {

        given()
                .pathParam("userId", userId)
                .when()
                .get()
                .then()
                .statusCode(400)
                .body(Matchers.is("You forgot the header followerId"));

    }

    @Test
    @DisplayName("should return 404 when follower does not exist")
    public void listFollowerNotFoundText() {

        var inexistentFollowerId = 999;

        given()
                .pathParam("userId", userId)
                .header("followerId", inexistentFollowerId)
                .when()
                .get()
                .then()
                .statusCode(400)
                .body(Matchers.is("Inexistent followerId"));

    }

    @Test
    @DisplayName("should return 203 when follower is not follower")
    public void listPostNotAFollower() {

        given()
                .pathParam("userId", userId)
                .header("followerId", userNotFollowerId)
                .when()
                .get()
                .then()
                .statusCode(403)
                .body(Matchers.is("You cannot see these posts."));


    }

    @Test
    @DisplayName("should list posts")
    public void listPostsTest() {
        given()
                .pathParam("userId", userId)
                .header("followerId", userFollowerId)
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("size()", Matchers.is(1));

    }
}