# com.brokenevent.nanotests
Lightweight JUnit-based HTTP/DB unit test extension.
Used to simplify site backend testing.

## Contents
Nanotests provides four types of assertion and helper classes for static import:

* [HttpAssert](http://brokenevent.com/docs/nanotests/com/brokenevent/nanotests/HttpAssert.html) provides asserts for the HTTP requests. It allows to check result codes, headers, content and others. You can use Asserts for the URLs or create a [TestRequest](http://brokenevent.com/docs/nanotests/com/brokenevent/nanotests/http/TestRequest.html) to check different conditions for a single HTTP request.

* [DbAssert](http://brokenevent.com/docs/nanotests/com/brokenevent/nanotests/DbAssert.html) provides asserts for the DataBase table rows states.

* [UrlAssert](http://brokenevent.com/docs/nanotests/com/brokenevent/nanotests/UrlAssert.html) provides asserts for the URLs. It allows to check URL generation algorithms.

* [XmlAssert](http://brokenevent.com/docs/nanotests/com/brokenevent/nanotests/XmlAssert.html) provides asserts for the XML DOM element states using XPath expressions.

## Install

1. Download and place in your classpath the following JAR: [nanotests.jar](http://brokenevent.com/docs/nanotests/nanotests.jar)
2. Resolve the following dependencies:

* junit:junit:4.12
* org.apache.httpcomponents:httpclient:4.4.1

## Usage examples

### DbAssert/HttpAssert

    import static com.brokenevent.nanotests.DbAssert.*;
    import static com.brokenevent.nanotests.HttpAssert.*;
    ...
    @Test
    public void testInstall(){
      assertHttpOk("/service/install/testProject/1.0");
      assertLastRow("installs", "id", "app", "testProject");
      assertLastRow("installs", "id", "version", "1.0");
      assertLastRow("installs", "id", "isinstall", true);
      assertHttpOk("/service/install/test Project/1.0.3.4.5");
      assertLastRow("installs", "id", "app", "test Project");
      assertLastRow("installs", "id", "version", "1.0.3.4.5");
      assertLastRow("installs", "id", "isinstall", true);
      assertHttpCode("/service/install/testProject", 400);
    }
    ...
    @Test
    public void testWrongValues(){
      assertHttp404("/service/exchange?id=testtesttest");

      TestHeadRequest request = new TestHeadRequest("/service/exchange?id=testtesttest");
      request.execute();

      assertHttpCode(request, 404);
      assertHttpNoHeader(request, "Revision");
    }

### XmlAssert

    import com.brokenevent.nanotests.http.TestGetRequest;
    import static com.brokenevent.nanotests.XmlAssert.*;
    import static com.brokenevent.nanotests.HttpAssert.*;
    ...
    @Test
    public void portCheckerTest() throws Exception {
      TestGetRequest request = createRequest("?ip=127.0.0.1&port=80");
      assertHttpCode(request, 200);
      Document doc = loadDocument(request.getStringContent());
      assertElementName(doc, "/Result/*", "Port");
      assertElementContent(doc, "Result/@host", "127.0.0.1");
      assertElementsCount(doc, "/Result/Port", 1);
      assertElementContent(doc, "/Result/Port/@port", "80");
      assertElementContent(doc, "/Result/Port/@status", "opened");
   }