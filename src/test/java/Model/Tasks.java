package Model;

import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
public class Tasks {

    /**

     Task 2
     create a request to https://httpstat.us/203
     expect status 203
     expect content type TEXT
     */
    @Test
    public void task2() {
        given()

                .when()
                .get("https://httpstat.us/203")

                .then()
                .log().all()
                .statusCode(203)
                .contentType(ContentType.TEXT)
        ;

    }

    /** Task 1

     create a request to https://jsonplaceholder.typicode.com/todos/2
     expect status 200
     Converting Into POJO */

    @Test
    public void task1() {
        LocationTask1 l =given()

                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")

                .then()
                .log().body()
                .statusCode(200)
                .extract().response().as(LocationTask1.class) ;

        System.out.println(l);
    }

    /**

     Task 3
     create a request to https://jsonplaceholder.typicode.com/todos/2
     expect status 200
     expect content type JSON
     expect title in response body to be "quis ut nam facilis et officia qui"
     */

    @Test
    public void task3() {
        given()

                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")

                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("title",equalTo("quis ut nam facilis et officia qui"))
                ;
    }


    /**

     Task 4
     create a request to https://jsonplaceholder.typicode.com/todos/2
     expect status 200
     expect content type JSON
     expect response completed status to be false
     extract completed field and testNG assertion
     */

    @Test
    public void task4() {

        given()

                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")

                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("completed", equalTo(false));


        boolean completed = given()

                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")

                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().path("completed");

        Assert.assertFalse(completed);

    }
}







