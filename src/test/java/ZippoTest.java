import Model.Location;
import Model.Place;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
public class ZippoTest {

    @Test
    public void test(){
     given()

             // Hazırlık işlemleri: (token, send body, parametreler)

             .when()

             // end point(URL), metod(get,post..)

             .then()

             // assertion, test, data işlemleri

             ;
    }

    @Test
    public void statusCodeTest(){

        given()



                .when()
                .get("http://api.zippopotam.us/us/90210")


                .then()
                .log().body()// dönen body json datası, log.all() her şeyi döndürür
                .statusCode(200) // dönüş kodu 200 mü
                 ;
    }

    @Test
    public void contentTypeTest(){

        given()


                .when()
                .get("http://api.zippopotam.us/us/90210")


                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON) // dönen body json mı?
        ;
    }

    @Test
    public void checkCountryInResponseBody(){

        given()


                .when()
                .get("http://api.zippopotam.us/us/90210")


                .then()
                .log().body()
                .statusCode(200)
                .body("country",equalTo("United States")) // body nin country değişkeni United States e eşit mi?

                // pm.response.json().id -> body.id
        ;
    }

    @Test
    public void checkStateInResponseBody(){

        given()

                .when()

                .then()
                .body("places[0].state",equalTo("California"))
        ;
    }

    @Test
    public void checkHasItem(){

        given()


                .when()
                .get("http://api.zippopotam.us/tr/01000")


                .then()
                .statusCode(200)
                .body("places.'place name'",hasItem("Dörtağaç Köyü")) // contains gibi çalışır. places ta place name i Dörtağaç Köyü olanlar, tümü içinde arar.
        ;
    }

    @Test
    public void bodyArrayHasSizeTest(){

        given()


                .when()
                .get("http://api.zippopotam.us/us/90210")


                .then()
                .statusCode(200)
                .body("places",hasSize(1)) // places in size 1 mi?
        ;
    }

    @Test
    public void combiningTest(){

        given()


                .when()
                .get("http://api.zippopotam.us/us/90210")


                .then()
                .statusCode(200)
                .body("places",hasSize(1))
                .body("places.state",hasItem("California"))
                .body("places[0].'place name'",equalTo("Beverly Hills")) // nokta atış sorduk places in 0. indeksindeki place name i istedik
        ;
    }

    @Test
    public void pathParamTest(){

        given()
                .pathParam("ulke","us")
                .pathParam("postaKodu",90210)
                .log().uri() // request link

                .when()
                .get("http://api.zippopotam.us/{ulke}/{postaKodu}")


                .then()
                .log().body()
                .statusCode(200)
        ;
    }

    @Test
    public void queryParamTest(){

        // https://gorest.co.in/public/v1/users?page=1

        given()
                .param("page",1)
                .log().uri() // request link

                .when()
                .get("https://gorest.co.in/public/v1/users")


                .then()
                .log().body()
                .statusCode(200)
        ;
    }

    @Test
    public void queryParamTest2(){

        // https://gorest.co.in/public/v1/users?page=3
        // bu linkteki 1 den 10 kadar sayfaları çağırdığınızda response daki donen page degerlerinin
        // çağrılan page nosu ile aynı olup olmadığını kontrol ediniz.

        for (int i = 1; i <= 10; i++) {


            given()
                    .param("page", i)
                    .log().uri() // request link

                    .when()
                    .get("https://gorest.co.in/public/v1/users")


                    .then()
                    .statusCode(200)
                    .body("meta.pagination.page",equalTo(i))
            ;
        }
    }

    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;

    @BeforeClass
    public void Setup(){

        baseURI = "https://gorest.co.in/public/v1";

        requestSpec = new RequestSpecBuilder()
                .log(LogDetail.URI)
                .setContentType(ContentType.JSON)
                .build();

        responseSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .log(LogDetail.BODY)
                .build();
    }


    @Test
    public void test1()
    {
        // https://gorest.co.in/public/v1/users?page=3

        given()
                .param("page",1)  // ?page=1  şeklinde linke ekleniyor
                .spec(requestSpec)

                .when()
                .get("/users")  // ?page=1

                .then()
                .spec(responseSpec)
        ;
    }


    @Test
    public void extractingJsonPath(){

        String countryName=
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .extract().path("country")
                ;

        System.out.println("countryName = " + countryName);
        Assert.assertEquals(countryName,"United States");
    }

    @Test
    public void extractingJsonPath2() {
        //placeName

        System.out.println(given()

                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log()
                .body()
                .extract().path("places[0].'place name'").toString()); // toString ile string değişkenine atamadan yazdırdık
                // öteki türlü string e atayıp onu yazdırabilirdik. Stringe atayınca otomatik casting yapıyor.
    }

    @Test
    public void extractingJsonPath3() {
        // https://gorest.co.in/public/v1/users  dönen değerdeki limit bilgisini yazdırınız.


       String limit = given()


                .when()
                .get("https://gorest.co.in/public/v1/users")


                .then()
                .log().body()
                .statusCode(200)
                .extract().path("meta.pagination.limit")
                ;

    }

    @Test
    public void extractingJsonPath4() {
        // https://gorest.co.in/public/v1/users  tüm idleri alın


        List<Integer> idler = given()


                .when()
                .get("https://gorest.co.in/public/v1/users")


                .then()
                .statusCode(200)
                .extract().path("data.id")
                ;

        System.out.println(idler);
    }

    @Test
    public void extractingJsonPath5() {
        // https://gorest.co.in/public/v1/users  tüm name alın


        List<String> nameler = given()


                .when()
                .get("https://gorest.co.in/public/v1/users")


                .then()
                .statusCode(200)
                .extract().path("data.name")
                ;

        System.out.println(nameler);
    }

    @Test
    public void extractingJsonPathResponsAll() {
        // https://gorest.co.in/public/v1/users  dönen değerdeki bütün name lei yazdırınız.

        Response donenData =
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .statusCode(200)
                        // .log().body()
                        .extract().response(); // dönen tüm datayı verir.
        ;

        List<Integer> idler= donenData.path("data.id");
        List<String> names= donenData.path("data.name");
        int limit= donenData.path("meta.pagination.limit");

        System.out.println("idler = " + idler);
        System.out.println("names = " + names);
        System.out.println("limit = " + limit);

        Assert.assertTrue(names.contains("Dakshayani Pandey"));
        Assert.assertTrue(idler.contains(1203767));
        Assert.assertEquals(limit, 10, "test sonucu hatalı");
    }

    @Test
    public void extractJsonAll_POJO() //Plain Old Java Object
    {
        Location locationNesnesi=
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        //.log().body()
                        .extract().body().as(Location.class)
                // // Location şablonuna
                ;

        System.out.println("locationNesnesi.getCountry() = " +
                locationNesnesi.getCountry());

        for(Place p: locationNesnesi.getPlaces())
            System.out.println("p = " + p);

        System.out.println(locationNesnesi.getPlaces().get(0).getPlacename());
    }

    @Test
    public void extractJsonAll_POJO_Soru()
    {
        Location location=
                given()
                        .when()
                        .get("http://api.zippopotam.us/tr/01000")

                        .then()
                        .extract().body().as(Location.class)
                        ;

        for (Place p: location.getPlaces()){
            if (p.getPlacename().equals("Dörtağaç Köyü")){
                System.out.println(p);
            }
        }
    }
}
