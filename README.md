# Custom RestTemplateBuilder
1. Builder 패턴을 활용한 RestTemplateBuilder<br>
2. Spring의 IoC를 활용한 싱글톤 RestTemplate
3. RestTemplate의 Customizing
4. RestTemplateBuilder 상속을 통한 기능 확장

```
[ 구조 ] 

TaehyeonRestTemple = RestTemplateBuilder + Taehyeon's Function    // Builder-pattern
                              |
                              |- HttpClient
                              |     ㄴ Connection Pool
                              |- HttpComponentsClientHttpRequestFactory
                              |     ㄴ Time Out
                              ㄴ CustomRestTemplateCustomizer
                                    ㄴ Logging
```

## Example
```java
@Autowired
private TaehyeonRestTempleBuilder taehyeonRestTempleBuilder;


/** GET 요청 예시(String return) **/
String result = taehyeonRestTempleBuilder
                .requestUriAndPath(DATA_BASE_URL
                        , "/VilageFcstInfoService_2.0/getVilageFcst"
                        , new LinkedMultiValueMap<>() {{
                            add("serviceKey", SERVICE_KEY);
                            add("pageNo", "1");
                            add("dataType", "JSON");
                            add("base_date", "20220207");
                            add("base_time", "1200");
                            add("nx", "57");
                            add("ny", "127");
                        }})
                .getForObject();

/** GET 요청 예시 **/
ResponseEntity<Object> result = taehyeonRestTempleBuilder
                .headers(new HashMap<>(){
                    {
                        put("X-Auth-Token", authKey);
                    }
                })
                .requestUriAndPath(crowdworksDefaultUrl
                        , "/project/output"
                        , new LinkedMultiValueMap<>() {{
                            add("queryingField", "");
                            add("queryingWord", "");
                            add("dataStatus", "");
                            add("edited", "");
                            add("rangingField", "");
                            add("page", String.valueOf(page));
                        }})
                .get();


/** POST 요청 예시 **/
ResponseEntity<Object> result = taehyeonRestTempleBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .body(loginInfo)
                .requestUriAndPath(apiUrl, loginpath, null)
                .post();


/** PATCH 요청 예시 **/
ResponseEntity<Object> result = taehyeonRestTempleBuilder
                .headers(new HashMap<>(){{
                    put("X-Auth-Token", authKey);
                }})
                .requestUriAndPath(apiUrl, authPath, null)
                .patch();

```

## 개발 목표
- 비즈니스 로직에서 구현하던 RestTemplate 생성과 처리 부분을 분리
- 추후 연계 시스템 개발에서도 해당 모듈을 사용하여 단순히 호출만을 통한 처리를 할 수 있게끔 개발
- Spring과 디자인 패턴을 공부하고 있는 것을 복습하며 실적용을 노림
- RestTemplate의 ConnectionPool 설정을 통해 3-Hankshake의 지연을 줄임 
<br>***(연계 서버이므로 API 호출 서버가 keep-alive 상태임을 가정 )***
- 기존의 Resttemplate 과 Custom한 TaehyeonRestTemplate을 공존하여 Client의 선택에 따라 사용할 수 있도록 개발
- jar 라이브러리화 하여 추후 연계 서버 개발시 개발 시간을 단축
- 회사 연계 공통 모듈로 사용할 수 있게??? :D
------
<br> 

## [개발 Note]
* 2022-02-22 
    - 프로젝트 Init
    - Builder patter 초기 동작 코드 작성
    - Get 요청시의 parameter를 명시적으로 주입하기 위해 MultiValueMap 과 UriComponentsBuilder를 활용하여 요청 URI를 생성
    - 모듈 자체 Exception 생성 (*수정 보완필요*)

* 2022-02-23
    - RestTemplate이 Connection Pool이 없이 요청할때마다 3-HandShack를 진행한다는 것을 공부, 이에 따라 Connection Pool을 설정하여 오버헤드를 줄임
    - application.properties 를 통한 Connection Pool과 TimeOut 설정을 할 수 있게 설정
    - 초기의 TaehyeonRestTemplateBuilder의 코드에 Connection Pool과 TimeOut 설정을 하여 Bean 생성을 하려고 하였으나 
  **' RestTemplateBuilder를 사용한 RestTemplate 설정 방법 : https://www.baeldung.com/spring-rest-template-builder '**
  을 보고 Customizing Logging을 추가 및 각 기능을 분리 시킨 Bean으로 생성하여 주입되어 Application Context가 자동 관리 되도록 함
    - RestTempleBuilder의 생성자에 CustomRestTemplateCustomizer가 parm으로 주입되는데 이에 따른 TaehyeonRestTemplateBuilder를
  RestTemplateBuilder를 상속 받아 기능을 확장 시키는 방향으로 전환
    - **(중요!!) 상속받은 Builder를 통한 build를 시키면 return이 super의 RestTemplate 이고, sub의 TaehyeonRestTempleBuilder로 casting이 안됨.**<br>
  해당 문제는 Builder 형태로 생성하고 마지막에 Casting을 하는 것이 아닌, Casting 한 Builder를 생성하고 생성한 객체에서 추가적인 작업을 진행<br>
      *(때문에 알고있던 Upcasting과 DownCasting 다시 공부....)*
    - 새롭게 만들어낸 TaehyeonRestTemplate과 기존의 RestTemplate을 공용으로 사용할 수 있도록 2가지 type의 Bean 생성하는 방향으로 생각중
    - 테스트 코드 추가


------
<br> 

## [개발 이슈]
1.  RestTemplate Get 요청시 String 으로 parameter를 주입시 자동 Encoding 처리 되어 요청 **[해결]** <br>

>> parmeter 를 URI로 주입하여 Encoding 되지 않게 처리

```java
restTemplate.getForObject(new URI(URLDecoder.decode(requestUri.toString(), "UTF-8")), String.class);
```

2. 상속받은 Builder를 통한 build를 시키면 return이 super의 RestTemplate 이고, sub의 TaehyeonRestTempleBuilder로 casting이 안됨 **[해결]** <br>
>> Casting 한 Builder를 생성하고 생성한 객체에서 추가적인 작업을 진행

```java
//불가능
TaehyeonRestTempleBuilder taehyeonRestTempleBuilder = (TaehyeonRestTempleBuilder)new TaehyeonRestTempleBuilder(customRestTemplateCustomizer())
        .requestFactory(() -> new BufferingClientHttpRequestFactory(factory))
        .additionalMessageConverters(new StringHttpMessageConverter(Charset.forName("UTF-8")));

// 가능
TaehyeonRestTempleBuilder taehyeonRestTempleBuilder = new TaehyeonRestTempleBuilder(customRestTemplateCustomizer());

taehyeonRestTempleBuilder.requestFactory(() -> new BufferingClientHttpRequestFactory(factory))
        .additionalMessageConverters(new StringHttpMessageConverter(Charset.forName("UTF-8")));
```
3. TaehyeonRestTempleBuilder 는 Builder인가 RestTemplate 인가.....?

 





