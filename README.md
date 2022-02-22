# RestTemplateBuilder
Builder 패턴을 활용한 RestTemplateBuilder

```
Builder-pattern + RestTemplate = RestTemplateBuilder
```

## Example
```java
/** GET 요청 예시(String return) **/
String result = new WiniRestTempleBuilder()
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
ResponseEntity<Object> result = new WiniRestTempleBuilder()
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
ResponseEntity<Object> result = new WiniRestTempleBuilder(loginInfo)
                .contentType(MediaType.APPLICATION_JSON)
                .body(loginInfo)
                .requestUriAndPath(apiUrl, loginpath, null)
                .post();


/** PATCH 요청 예시 **/
ResponseEntity<Object> result = new WiniRestTempleBuilder()
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

------
<br> 

## [개발 Note]
* 2022-02-22 
    - 프로젝트 Init
    - Builder patter 초기 동작 코드 작성
    - Get 요청시의 parameter를 명시적으로 주입하기 위해 MultiValueMap 과 UriComponentsBuilder를 활용하여 요청 URI를 생성
    - 모듈 자체 Exception 생성 (*수정 보완필요*)


------
<br> 

## [개발 이슈]
1.  RestTemplate Get 요청시 String 으로 parameter를 주입시 자동 Encoding 처리 되어 요청 **[해결]** <br>

>> parmeter 를 URI로 주입하여 Encoding 되지 않게 처리

```java
restTemplate.getForObject(new URI(URLDecoder.decode(requestUri.toString(), "UTF-8")), String.class);
```



 





