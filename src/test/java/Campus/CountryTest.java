package Campus;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CountryTest {

    String countryID;
    RequestSpecification recSpec;

    Faker faker=new Faker();
    String countryName = faker.address().country()+"Ress";
    String countryCode = faker.address().countryCode()+"Ress";
    Map<String,String> country=new HashMap<>();


    @BeforeClass
    public void Setup()  {
        baseURI="https://test.mersys.io";

        Map<String,String> userCredential=new HashMap<>();
        userCredential.put("username","turkeyts");
        userCredential.put("password","TechnoStudy123");
        userCredential.put("rememberMe","true");

        Cookies cookies=
        given()
                .contentType(ContentType.JSON)
                .body(userCredential)

                .when()
                .post("/auth/login")

                .then()
                //.log().all()
                .statusCode(200)
                .extract().response().getDetailedCookies()
        ;

        recSpec= new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addCookies(cookies)
                .build();
    }

    @Test
    public void createCountry()  {

        country.put("name",countryName);
        country.put("code",countryCode);

        countryID=
        given()
                .spec(recSpec)
                .body(country)
                .log().body()

                .when()
                .post("/school-service/api/countries")

                .then()
                .log().body()
                .statusCode(201)
                .extract().path("id");


        System.out.println("countryID = " + countryID);
    }

    @Test(dependsOnMethods = "createCountry")
    public void createCountryNegative()  {


        country.put("name",countryName);
        country.put("code",countryCode);

                given()
                        .spec(recSpec)
                        .body(country)  // giden body
                        .log().body()  // giden body yi log olarak göster

                        .when()
                        .post("/school-service/api/countries")

                        .then()
                        //.log().body()
                        .statusCode(400)
                        .body("message", containsString("already"))  // gelen body deki...
                ;
    }


    @Test(dependsOnMethods = "createCountryNegative")
    public void updateCountry()  {

        countryName+="R";
        country.put("id",countryID);
        country.put("name",countryName);
        country.put("code",countryCode);

        given()
                .spec(recSpec)
                .body(country)
                //.log().body()

                .when()
                .put("/school-service/api/countries")

                .then()
                //.log().body()
                .statusCode(200)
                .body("name", equalTo(countryName))
        ;
    }


    @Test(dependsOnMethods = "updateCountry")
    public void deleteCountry()  {

        given()
                .spec(recSpec)
                .pathParam("countryID",countryID)
                .log().uri()

                .when()
                .delete("/school-service/api/countries/{countryID}")// pathparam da olur aşağıdaki de olur.

                .then()
                .log().body() // gelen body yi log olarak göster
                .statusCode(200);
    }

    @Test(dependsOnMethods = "deleteCountry")
    public void deleteCountryNegative()  {  given()
            .spec(recSpec)

            .when()
            .delete("/school-service/api/countries/"+countryID)

            .then()
            .statusCode(400)
            .body("message",equalTo("Country not found"))
    ;
    }

    // TODO : CtizenShip  API Automation nı yapınız (create, createNegative, update, delete, deleteNegative)
}
