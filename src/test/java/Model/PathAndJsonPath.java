package Model;

import GoRest.User;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

public class PathAndJsonPath {

    @Test
    public void extractingPath() {
        // "post code": "90210",

        int postCode =
                given()

                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .log().body()
                        .extract().path("'post code'");
        ;

        System.out.println("postCode = " + postCode);
    }

    @Test
    public void extractingJsonPath() {
        // "post code": "90210",
        int postCode =
                given()

                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .log().body()
                        .extract().jsonPath().getInt("'post code'")
                // tip dönüşümü otomatik, uygun tip verilmeli
                ;

        System.out.println("postCode = " + postCode);
    }

    @Test
    public void getUsers() {
        Response response =
                given()

                        .when()
                        .get("https://gorest.co.in/public/v2/users")

                        .then()
                        //.log().body()
                        .extract().response();
        ;

        int idPath = response.path("[2].id");
        int idJsonPath = response.jsonPath().getInt("[2].id");

        System.out.println("idPath = " + idPath);
        System.out.println("idJsonPath = " + idJsonPath);

        User[] usersPath= response.as(User[].class);
        List<User> usersJsonPath= response.jsonPath().getList("", User.class);

        System.out.println("usersPath = " + Arrays.toString(usersPath));
        System.out.println("usersJsonPath = " + usersJsonPath);
    }

    @Test
    public void getUsersV1()
    {
        Response body=
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        //.log().body()
                        .extract().response();
        ;

        List<User> dataUsers= body.jsonPath().getList("data", User.class);
        // JSONPATH bir response içindeki bir parçayı nesneye ödnüştürebiliriz.
        System.out.println("dataUsers = " + dataUsers);

        // Daha önceki örneklerde as ile Class dönüşümlerini yapıyorduk ve
        // tüm yapıya karşılık gelen classları yazarak dönüştürüp istediğimiz elemanlara ulaşıyorduk.
        // Burada ise(JsonPath ile) aradaki bir veriyi clasa dönüştürerek bir list olarak alıyoruz
        // Böylece tek class ile veri alınmış oldu ve diğer classlara gerek kalmadı

        // path : class veya tip dönüşümüne imkan vermez, direkt veriyi verir. List<String> gibi
        // jsonPath : class dönüşümüne ve tip dönüşümüne izin vererek veriyi istediğimiz formatta verir.
    }

    @Test
    public void getZipCode(){
        Response response=
                given()

                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        //.log().body()
                        .extract().response()
                ;

        Location locPathAs = response.as(Location.class); // Bütün classları yazmak zorundasın
        System.out.println("locPathAs.get = " + locPathAs.getPlaces());

        List<Place> places= response.jsonPath().getList("places", Place.class); // nokta atışı istediğimiz nesneyi aldık.
        System.out.println("places = " + places);
    }



}
